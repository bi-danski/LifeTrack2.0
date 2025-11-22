package org.lifetrack.ltapp.model.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class UserDataResponse @OptIn(ExperimentalTime::class) constructor(
    val fullName: String?,
    val phoneNumber: Long,
    val userName: String,
    val emailAddress: String,
    @Contextual val createdAt: Instant
)

data class LoginRequest(
    val emailAddress: String,
    val password: String
)
