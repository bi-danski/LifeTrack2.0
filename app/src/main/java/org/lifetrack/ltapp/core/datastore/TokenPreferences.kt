package org.lifetrack.ltapp.core.datastore

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.lifetrack.ltapp.core.security.crypto.CryptoGCM
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object TokenPreferenceSerializer : Serializer<TokenPreferences> {
    override val defaultValue: TokenPreferences
        get() = TokenPreferences()

    override suspend fun readFrom(input: InputStream): TokenPreferences {
        return try {
            val encryptedBytes = input.readBytes()
            if (encryptedBytes.isEmpty()) return defaultValue

            val decryptedBytes = CryptoGCM.doDecrypt(encryptedBytes)
            Json.decodeFromString(
                deserializer = TokenPreferences.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: TokenPreferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val bytes = Json.encodeToString(TokenPreferences.serializer(), t).toByteArray()

                try {
                    CryptoGCM.doEncrypt(bytes)
                } catch (e: Exception) {
                    if (isKeyInvalidated(e)) {
                        CryptoGCM.deleteKey()
                        CryptoGCM.doEncrypt(bytes)
                    } else {
                        throw e
                    }
                }
            }.onSuccess { encryptedBytes ->
                output.write(encryptedBytes)
            }.onFailure { exception ->
                throw IOException("Unable to safely encrypt or write AccessPreferences", exception)
            }
        }
    }

    private fun isKeyInvalidated(e: Exception): Boolean {
        return e is android.security.keystore.KeyPermanentlyInvalidatedException ||
                e is java.security.UnrecoverableKeyException ||
                e.message?.contains("Key invalidated", ignoreCase = true) == true
    }
}