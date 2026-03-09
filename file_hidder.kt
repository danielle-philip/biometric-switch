package com.example.dualfingerprint

import android.content.Context
import java.io.File
import java.io.IOException

object FileHider {

    fun hideSensitiveItems(context: Context, sourcePaths: List<String>) {
        val hiddenDir = File(context.filesDir, "hidden_items")
        if (!hiddenDir.exists()) hiddenDir.mkdirs()

        sourcePaths.forEach { path ->
            try {
                val src = File(path)
                if (src.exists()) {
                    val dst = File(hiddenDir, src.name)
                    src.copyTo(dst, overwrite = true)
                    src.delete()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun unhideSensitiveItems(context: Context, targetDir: File) {
        val hiddenDir = File(context.filesDir, "hidden_items")
        if (!hiddenDir.exists()) return

        hiddenDir.listFiles()?.forEach { hiddenFile ->
            val dst = File(targetDir, hiddenFile.name)
            hiddenFile.copyTo(dst, overwrite = true)
            hiddenFile.delete()
        }
    }
}
