package com.example.dualfingerprint

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var bioHelper: BiometricHelper

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
                    FileHider.hideSensitiveItems(this, filesToHide)
                },
                onFail = { /* show a Toast or log */ }
            )
        }

        btnNormal.setOnClickListener {
            bioHelper.promptFingerprint(
                forPanic = false,
                onSuccess = {
                    FileHider.unhideSensitiveItems(this, File("/sdcard/Restored"))
                },
                onFail = { /* show a Toast or log */ }
            )
        }
    }
}
