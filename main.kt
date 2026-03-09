package com.example.dualfingerprint

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var bioHelper: BiometricHelper
    private val ioExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bioHelper = BiometricHelper(this)
        bioHelper.createKeys()

        val btnPanic = findViewById<Button>(R.id.btnPanic)
        val btnNormal = findViewById<Button>(R.id.btnNormal)

        btnPanic.setOnClickListener {
            bioHelper.promptFingerprint(
                forPanic = true,
                onSuccess = {
                    val filesToHide = listOf(
                        "/sdcard/DCIM/Camera/private1.jpg",
                        "/sdcard/Download/confidential.pdf"
                    )
                    ioExecutor.execute {
                        val result = FileHider.hideSensitiveItems(this, filesToHide)
                        runOnUiThread {
                            val msg = if (result.successCount > 0) {
                                "Hidden ${result.successCount} item(s)."
                            } else {
                                "No files were hidden."
                            }
                            showToast(withFailureSuffix(msg, result.failedPaths))
                        }
                    }
                },
                onFail = { showToast("Panic authentication failed or unavailable.") }
            )
        }

        btnNormal.setOnClickListener {
            bioHelper.promptFingerprint(
                forPanic = false,
                onSuccess = {
                    ioExecutor.execute {
                        val restoreDir = getExternalFilesDir(null)?.let { File(it, "Restored") }
                            ?: File(filesDir, "Restored")
                        val result = FileHider.unhideSensitiveItems(this, restoreDir)
                        runOnUiThread {
                            val msg = if (result.successCount > 0) {
                                "Restored ${result.successCount} item(s)."
                            } else {
                                "No hidden files found to restore."
                            }
                            showToast(withFailureSuffix(msg, result.failedPaths))
                        }
                    }
                },
                onFail = { showToast("Normal authentication failed or unavailable.") }
            )
        }
    }

    override fun onDestroy() {
        ioExecutor.shutdown()
        super.onDestroy()
    }

    private fun withFailureSuffix(base: String, failedPaths: List<String>): String {
        return if (failedPaths.isEmpty()) {
            base
        } else {
            "$base Failed: ${failedPaths.size}."
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
