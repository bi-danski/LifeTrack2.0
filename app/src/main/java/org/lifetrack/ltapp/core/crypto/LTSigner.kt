package org.lifetrack.ltapp.core.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

@Suppress("unused")
object LTSigner {
    private const val SIGNING_ALIAS = "LT_SIGNING_KEY"
    private const val ANDROID_KEY_STORE = "AndroidKeyStore"

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
    }

    private fun getPrivateKey(): PrivateKey {
        val entry = keyStore
            .getEntry(SIGNING_ALIAS, null) as? KeyStore.PrivateKeyEntry
        return entry?.privateKey ?: generateKeyPair().private
    }

    private fun generateKeyPair(): KeyPair {
        val kpg = KeyPairGenerator.getInstance(
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
        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initSign(getPrivateKey())
        signature.update(data)
        return signature.sign()
    }

    fun verifyData(data: ByteArray, signatureBytes: ByteArray, publicKey: PublicKey): Boolean {
        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initVerify(publicKey)
        signature.update(data)
        return signature.verify(signatureBytes)
    }
}
