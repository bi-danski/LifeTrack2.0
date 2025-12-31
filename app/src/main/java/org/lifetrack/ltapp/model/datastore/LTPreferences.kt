package org.lifetrack.ltapp.model.datastore

import android.security.keystore.KeyPermanentlyInvalidatedException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.lifetrack.ltapp.core.security.crypto.CryptoGCM
import org.lifetrack.ltapp.model.data.dclass.LTPreferences
import java.io.InputStream
import java.io.OutputStream
import java.security.UnrecoverableKeyException


object LTPreferenceSerializer : Serializer<LTPreferences> {
    override val defaultValue: LTPreferences
        get() = LTPreferences()

    override suspend fun readFrom(input: InputStream): LTPreferences {
        return try {
            val encryptedBytes = input.readBytes()

            if (encryptedBytes.isEmpty()) {
                return defaultValue
            }
            val decryptedBytes = CryptoGCM.doDecrypt(encryptedBytes)
            Json.decodeFromString(
                deserializer = LTPreferences.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch (e: Exception) {
            println(e.printStackTrace())
            defaultValue
        }
    }

    override suspend fun writeTo(t: LTPreferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            runCatching {
                val jsonString = Json.encodeToString(LTPreferences.serializer(), t)
                val bytes = jsonString.toByteArray()
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
                throw kotlinx.io.IOException("Final attempt to write preferences failed", exception)
            }
        }
    }

    private fun isKeyInvalidated(e: Exception): Boolean {
        return e is KeyPermanentlyInvalidatedException ||
                e is UnrecoverableKeyException ||
                e.message?.contains("Key invalidated", ignoreCase = true) == true
    }
}