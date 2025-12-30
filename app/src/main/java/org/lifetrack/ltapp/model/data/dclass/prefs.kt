package org.lifetrack.ltapp.model.data.dclass

import kotlinx.serialization.Serializable

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