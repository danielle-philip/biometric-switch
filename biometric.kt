package com.example.dualfingerprint

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class BiometricHelper(private val activity: FragmentActivity) {

    private val keyStoreName = "AndroidKeyStore"
    private val keyAliasA = "key_normal"
    private val keyAliasB = "key_panic"

    fun createKeys() {
        createKeyIfMissing(keyAliasA)
        createKeyIfMissing(keyAliasB)
    }

    private fun createKeyIfMissing(alias: String) {
        val keyStore = KeyStore.getInstance(keyStoreName).apply { load(null) }
        if (!keyStore.containsAlias(alias)) {
            val generator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, keyStoreName
            )
            val spec = KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(true)
                .build()
            generator.init(spec)
            generator.generateKey()
        }
    }

    private fun getCipher(alias: String): Cipher {
        val keyStore = KeyStore.getInstance(keyStoreName).apply { load(null) }
        val secretKey = keyStore.getKey(alias, null) as SecretKey
        return Cipher.getInstance(
            "${KeyProperties.KEY_ALGORITHM_AES}/" +
                    "${KeyProperties.BLOCK_MODE_CBC}/" +
                    KeyProperties.ENCRYPTION_PADDING_PKCS7
        ).apply { init(Cipher.ENCRYPT_MODE, secretKey) }
    }

    fun promptFingerprint(
        forPanic: Boolean,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        val alias = if (forPanic) keyAliasB else keyAliasA
        val cipher = getCipher(alias)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(if (forPanic) "Panic Fingerprint" else "Normal Fingerprint")
            .setSubtitle("Authenticate using fingerprint")
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) = onSuccess()

                override fun onAuthenticationError(code: Int, msg: CharSequence) = onFail()
                override fun onAuthenticationFailed() = onFail()
            }
        )

        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }
}
