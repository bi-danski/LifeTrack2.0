package org.lifetrack.ltapp.model.data.dto

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

@Serializable
data class LoginRequest(
    val emailAddress: String,
    val password: String
)

@Serializable
data class SignUpRequest(
    val fullName: String,
    val userName: String,
    val password: String,
    val emailAddress: String,
    val phoneNumber: Long,
)

@Serializable
data class SignUpResponse(
    val fullName: String?,
    val phoneNumber: Long,
    val userName: String,
    val emailAddress: String,
    val createdAt: Instant
)

@Serializable
data class PwdRestoreRequest(
    val emailAddress: String
)

//@Serializable
//data class PwdRestoreResponse(
//    val message: String
//)