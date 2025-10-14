package org.lifetrack.ltapp.model.data

import kotlinx.serialization.Serializable

@Serializable
data class CohereResponse(
    val generations: List<CohereGeneration>? = null,
    val message: String? = null,
    val statusCode: Int? = null
)

@Serializable
data class CohereRequest(
    val model: String = "command-r",
    val prompt: String,
    val maxTokens: Int? = 300,
    val temperature: Float? = null,
    val k: Int? = null,
    val p: Float? = null,
    val frequencyPenalty: Float? = null,
    val presencePenalty: Float? = null,
    val stopSequences: List<String>? = null
)
@Serializable
data class CohereGeneration(
    val text: String
)

@Serializable
data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

data class Message(
    val id: String,
    val text: String,
    val isFromPatient: Boolean,
    val timestamp: String
)