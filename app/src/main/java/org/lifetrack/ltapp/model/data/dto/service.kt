package org.lifetrack.ltapp.model.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AssistantResult(
    val output: AssistantOutput
)

@Serializable
data class AssistantOutput(
    val messageType: String,
    val text: String,
    val metadata: MessageMetadata
)

@Serializable
data class MessageMetadata(
    val finishReason: String,
    val candidateIndex: Int
)

@Serializable
data class UserPrompt(
    val chatId: String,
    val prompt: String
)