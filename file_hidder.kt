package com.example.dualfingerprint

import android.content.Context
import java.io.File
import java.io.IOException

object FileHider {

    data class OperationResult(
        val successCount: Int,
        val failedPaths: List<String>
    )

    fun hideSensitiveItems(context: Context, sourcePaths: List<String>): OperationResult {
        val hiddenDir = File(context.filesDir, "hidden_items")
        if (!hiddenDir.exists() && !hiddenDir.mkdirs()) {
            return OperationResult(0, sourcePaths)
        }

        var successCount = 0
        val failedPaths = mutableListOf<String>()

        sourcePaths.forEach { path ->
            val src = File(path)
            if (!src.exists() || !src.isFile) {
                failedPaths += path
                return@forEach
            }

            val dst = File(hiddenDir, src.name)
            if (moveSafely(src, dst)) {
                successCount++
            } else {
                failedPaths += path
            }
        }

        return OperationResult(successCount, failedPaths)
    }

    fun unhideSensitiveItems(context: Context, targetDir: File): OperationResult {
        val hiddenDir = File(context.filesDir, "hidden_items")
        if (!hiddenDir.exists()) return OperationResult(0, emptyList())

        if (!targetDir.exists() && !targetDir.mkdirs()) {
            val failed = hiddenDir.listFiles()?.map { it.absolutePath } ?: emptyList()
            return OperationResult(0, failed)
        }

        var successCount = 0
        val failedPaths = mutableListOf<String>()

        hiddenDir.listFiles()?.filter { it.isFile }?.forEach { hiddenFile ->
            val dst = File(targetDir, hiddenFile.name)
            if (moveSafely(hiddenFile, dst)) {
                successCount++
            } else {
                failedPaths += hiddenFile.absolutePath
            }
        }

        return OperationResult(successCount, failedPaths)
    }

    private fun moveSafely(source: File, destination: File): Boolean {
        return try {
            val parent = destination.parentFile ?: return false
            if (!parent.exists() && !parent.mkdirs()) return false

            val temp = File(parent, "${destination.name}.tmp")
            if (temp.exists() && !temp.delete()) return false

            source.copyTo(temp, overwrite = true)

            val destinationReady = if (temp.renameTo(destination)) {
                true
            } else {
                temp.copyTo(destination, overwrite = true)
                temp.delete()
                true
            }

            if (!destinationReady) return false

            if (!source.delete()) {
                destination.delete()
                return false
            }

            true
        } catch (e: IOException) {
            false
        }
    }
}
