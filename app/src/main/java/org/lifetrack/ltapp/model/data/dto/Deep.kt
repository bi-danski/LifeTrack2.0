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