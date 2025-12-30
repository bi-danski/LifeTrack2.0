package org.lifetrack.ltapp.core.security.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore

object LTSigner {
    private const val SIGNING_ALIAS = "LT_SIGNING_KEY"
    private const val ANDROID_KEY_STORE = "AndroidKeyStore"

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
    }

    private fun getPrivateKey(): java.security.PrivateKey {
        val entry = keyStore
            .getEntry(SIGNING_ALIAS, null) as? KeyStore.PrivateKeyEntry
        return entry?.privateKey ?: generateKeyPair().private
    }

    private fun generateKeyPair(): java.security.KeyPair {
        val kpg = java.security.KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            ANDROID_KEY_STORE
        )
        val spec = KeyGenParameterSpec.Builder(
            SIGNING_ALIAS,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setUserAuthenticationRequired(false)
            .build()

        kpg.initialize(spec)
        return kpg.generateKeyPair()
    }

    fun signData(data: ByteArray): ByteArray {
        val signature = java.security.Signature.getInstance("SHA256withECDSA")
        signature.initSign(getPrivateKey())
        signature.update(data)
        return signature.sign()
    }

    fun verifyData(data: ByteArray, signatureBytes: ByteArray, publicKey: java.security.PublicKey): Boolean {
        val signature = java.security.Signature.getInstance("SHA256withECDSA")
        signature.initVerify(publicKey)
        signature.update(data)
        return signature.verify(signatureBytes)
    }
}
