package org.lifetrack.ltapp.model.data.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import kotlin.time.Instant

@Serializable
data class UserDataResponse(
    val fullName: String?,
    val phoneNumber: Long,
    val userName: String,
    val emailAddress: String,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val role: String,
)

@Serializable
data class SessionInfo(
    val accessToken: String,
    val refreshToken: String,
    val agent47: UserDataResponse
)

@Serializable
data class UserDataUpdate(
    val fullName: String?,
    val phoneNumber: Long?,
    val userName: String?,
    val emailAddress: String?,
    val updatedAt: Instant?,
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
data class PwdRestoreRequest(
    val emailAddress: String
)

@Serializable
data class AppointmentUpdate(
    val id: String? = null,
    val doctor: String,
    val hospital: String,
    val status: AppointmentStatus,
    @Contextual val scheduledAt: LocalDateTime,
    @Contextual val bookedAt: Instant
)

@Serializable
enum class AppointmentStatus {
    UPCOMING,
    ATTENDED,
    RECENTLY_BOOKED,
    RESCHEDULED,
    DISMISSED,
    CANCELLED
}