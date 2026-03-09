package com.example.dualfingerprint

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
                    ioExecutor.execute {
                        val result = DuressPolicy.activateDuress(this)
                        runOnUiThread {
                            showToast(
                                "Duress mode active. Hidden: ${result.hiddenCount}, Failed: ${result.failedCount}."
                            )
                        }
                    }
                },
                onFail = { showToast("Duress authentication failed or unavailable.") }
            )
        }

        btnNormal.setOnClickListener {
            bioHelper.promptFingerprint(
                forPanic = false,
                onSuccess = {
                    ioExecutor.execute {
                        val result = DuressPolicy.activateNormal(this)
                        runOnUiThread {
                            showToast(
                                "Normal mode active. Restored: ${result.restoredCount}, Failed: ${result.failedCount}."
                            )
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
