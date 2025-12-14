package org.lifetrack.ltapp.model.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class AlmaMessage(
    val text: String,
    val check: Boolean? = false,
    val isUser: Boolean = true
): Parcelable

@Serializable
data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@Serializable
data class Message(
    val id: String,
    val text: String,
    val isFromPatient: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)