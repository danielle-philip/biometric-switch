package com.example.dualfingerprint

import android.content.Context
import java.io.File

enum class SecurityMode {
    NORMAL,
    DURESS
}

data class DuressResult(
    val mode: SecurityMode,
    val hiddenCount: Int = 0,
    val restoredCount: Int = 0,
    val failedCount: Int = 0
)

object DuressPolicy {

    private val defaultSensitivePaths = listOf(
        "/sdcard/DCIM/Camera/private1.jpg",
        "/sdcard/Download/confidential.pdf"
    )

    fun activateDuress(context: Context): DuressResult {
        val hideResult = FileHider.hideSensitiveItems(context, defaultSensitivePaths)
        return DuressResult(
            mode = SecurityMode.DURESS,
            hiddenCount = hideResult.successCount,
            failedCount = hideResult.failedPaths.size
        )
    }

    fun activateNormal(context: Context): DuressResult {
        val restoreDir = context.getExternalFilesDir(null)?.let { File(it, "Restored") }
            ?: File(context.filesDir, "Restored")
        val restoreResult = FileHider.unhideSensitiveItems(context, restoreDir)
        return DuressResult(
            mode = SecurityMode.NORMAL,
            restoredCount = restoreResult.successCount,
            failedCount = restoreResult.failedPaths.size
        )
    }
}
