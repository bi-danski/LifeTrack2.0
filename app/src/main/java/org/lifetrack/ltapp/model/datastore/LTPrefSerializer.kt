package org.lifetrack.ltapp.model.datastore

import android.security.keystore.KeyPermanentlyInvalidatedException
import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.lifetrack.ltapp.core.security.crypto.CryptoGCM
import org.lifetrack.ltapp.model.data.dclass.AppPreferences
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.UnrecoverableKeyException

object LTPrefSerializer : Serializer<AppPreferences> {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        coerceInputValues = true
    }

    override val defaultValue: AppPreferences
        get() = AppPreferences()

    override suspend fun readFrom(input: InputStream): AppPreferences {
        val encryptedBytes = input.readBytes()
        if (encryptedBytes.isEmpty()) return defaultValue

        return try {
            val decryptedBytes = CryptoGCM.doDecrypt(encryptedBytes)
            json.decodeFromString(
                deserializer = AppPreferences.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch (e: SerializationException) {
            Log.e("AppSerializer", "Failed to deserialize AppPreferences", e)
            throw CorruptionException("Data structure mismatch in AppPreferences", e)
        } catch (e: Exception) {
            Log.e("AppSerializer", "Decryption failed. Keystore might be out of sync.", e)
            if (isKeyInvalidated(e)) defaultValue else throw IOException(e)
        }
    }

    override suspend fun writeTo(t: AppPreferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            try {
                val jsonString = json.encodeToString(AppPreferences.serializer(), t)
                val bytes = jsonString.toByteArray()

                val encryptedBytes = try {
                    CryptoGCM.doEncrypt(bytes)
                } catch (e: Exception) {
                    if (isKeyInvalidated(e)) {
                        Log.w("AppSerializer", "Key invalidated. Regenerating for new write.")
                        CryptoGCM.deleteKey()
                        CryptoGCM.doEncrypt(bytes)
                    } else {
                        throw e
                    }
                }
                output.write(encryptedBytes)
            } catch (e: Exception) {
                Log.e("AppSerializer", "Failed to write encrypted preferences", e)
                throw IOException("Unable to safely encrypt or write AppPreferences", e)
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