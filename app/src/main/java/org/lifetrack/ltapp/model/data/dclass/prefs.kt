package org.lifetrack.ltapp.model.data.dclass

import kotlinx.serialization.Serializable
import kotlin.time.Instant


@Serializable
data class LTPreferences(
    val appNotificationsEnabled: Boolean = true,
    val appEmailNotificationsEnabled: Boolean = false,
    val appSmsNotificationsEnabled: Boolean = false,
    val appAnimationsEnabled: Boolean = true,
    val userPatientDataConsentEnabled: Boolean = false,
    val appReminderNotificationsEnabled: Boolean = true
)

@Serializable
data class TokenPreferences(
    val accessToken: String? = null,
    val refreshToken: String? = null
)

@Serializable
data class UserPreferences(
    val userName: String = "",
    val userEmail: String = "",
    val userFullName: String = "",
    val userInitials: String = "",
    val userPhoneNumber: String = "",
    val updatedAt: Instant? = null,
    val createdAt: Instant? = null,
    val lastLoginAt: Instant? = null
) {
    val isDefault: Boolean get() = userEmail.isBlank()
}