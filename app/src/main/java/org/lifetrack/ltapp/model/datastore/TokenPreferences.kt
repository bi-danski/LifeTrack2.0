package org.lifetrack.ltapp.model.datastore

import android.security.keystore.KeyPermanentlyInvalidatedException
import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.json.Json
import org.lifetrack.ltapp.core.security.crypto.CryptoGCM
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.UnrecoverableKeyException

object TokenPreferenceSerializer : Serializer<TokenPreferences> {
    override val defaultValue: TokenPreferences = TokenPreferences()

    override suspend fun readFrom(input: InputStream): TokenPreferences {
        val encryptedBytes = input.readBytes()
        if (encryptedBytes.isEmpty()) return defaultValue

        return try {
            val decryptedBytes = CryptoGCM.doDecrypt(encryptedBytes)
            Json.decodeFromString(
                deserializer = TokenPreferences.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch (e: Exception) {
            Log.e("TokenSerializer", "Decryption failed. Data may be locked or corrupted.", e)

            if (isKeyInvalidated(e)) {
                throw CorruptionException("Keystore key invalidated, cannot decrypt tokens", e)
            }
            throw IOException("Could not read encrypted preferences", e)
        }
    }

    override suspend fun writeTo(t: TokenPreferences, output: OutputStream) {
        val bytes = Json.encodeToString(TokenPreferences.serializer(), t).toByteArray()

        try {
            val encryptedBytes = CryptoGCM.doEncrypt(bytes)
            output.write(encryptedBytes)
        } catch (e: Exception) {
            if (isKeyInvalidated(e)) {
                Log.w("TokenSerializer", "Key invalidated during write. Regenerating key...")
                CryptoGCM.deleteKey()
                val retryBytes = CryptoGCM.doEncrypt(bytes)
                output.write(retryBytes)
            } else {
                throw IOException("Encryption failed permanently during write", e)
            }
        }
    }

    private fun isKeyInvalidated(e: Exception): Boolean {
        return e is KeyPermanentlyInvalidatedException ||
                e is UnrecoverableKeyException ||
                e.message?.contains("Key invalidated", ignoreCase = true) == true ||
                e.message?.contains("User authentication", ignoreCase = true) == true
    }
}