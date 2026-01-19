package org.lifetrack.ltapp.core.service


import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import org.lifetrack.ltapp.model.data.dto.AssistantResult
import org.lifetrack.ltapp.model.data.dto.UserPrompt


class AlmaService(
    private val httpClient: HttpClient
) {

    suspend fun promptAssistant(userPrompt: UserPrompt): String {
        return try {
            val response = httpClient.post("/alma/chat/") {
                contentType(ContentType.Application.Json)
                setBody(userPrompt)
            }
            val result = response.body<AssistantResult>()
            result.output.text.trim()

        } catch (e: Exception) {
            Log.e( "promptAssistant()", "${e.localizedMessage}")
            "${e.message}"
        }
    }
}

