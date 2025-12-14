package org.lifetrack.ltapp.model.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeepSeekRequest(
    val model: String,
    val messages: List<DeepSeekMessage>
)

@Serializable
data class DeepSeekMessage(
    val role: String,
    val content: String
)

@Serializable
data class DeepSeekResponse(
    val choices: List<DeepSeekChoice>? = null,
    val error: DeepSeekError? = null
)

@Serializable
data class DeepSeekChoice(
    val message: DeepSeekMessage
)

@Serializable
data class DeepSeekError(
    val message: String,
    val type: String,
    val code: String? = null
)

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