package org.lifetrack.ltapp.model.data.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class UserDataResponse @OptIn(ExperimentalTime::class) constructor(
    val fullName: String?,
    val phoneNumber: Long,
    val userName: String,
    val emailAddress: String,
    @Contextual val createdAt: kotlin.time.Instant
)

data class LoginRequest(
    val emailAddress: String,
    val password: String
)
