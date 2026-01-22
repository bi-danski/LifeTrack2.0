package org.lifetrack.ltapp.model.datastore

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.lifetrack.ltapp.core.security.crypto.CryptoGCM
import org.lifetrack.ltapp.model.data.dclass.UserPreferences
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object UserPreferenceSerializer : Serializer<UserPreferences> {

    private val serializerOwnJson = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override val defaultValue: UserPreferences
        get() = UserPreferences()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        val encryptedBytes = input.readBytes()
        if (encryptedBytes.isEmpty()) return defaultValue

        return try {
            val decryptedBytes = CryptoGCM.doDecrypt(encryptedBytes)
            serializerOwnJson.decodeFromString(
                deserializer = UserPreferences.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        try {
            val jsonString = serializerOwnJson.encodeToString(UserPreferences.serializer(), t)
            val bytes = jsonString.toByteArray()

            val encryptedBytes = CryptoGCM.doEncrypt(bytes)
            output.write(encryptedBytes)
        } catch (e: SerializationException) {
            throw IOException("Unable to serialize UserPreferences", e)
        } catch (e: Exception) {
            throw IOException("Unable to safely encrypt or write UserPreferences", e)
        }
    }
}