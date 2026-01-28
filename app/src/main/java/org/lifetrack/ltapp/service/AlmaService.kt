package org.lifetrack.ltapp.service


import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.lifetrack.ltapp.model.data.dto.AssistantResult
import org.lifetrack.ltapp.model.data.dto.UserPrompt


class AlmaService(
    private val httpClient: HttpClient
) {

    suspend fun promptAssistant(userPrompt: UserPrompt): String {
        return try {
            val response = httpClient.post("/alma/chat") {
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

