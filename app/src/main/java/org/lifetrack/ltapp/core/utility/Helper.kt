package org.lifetrack.ltapp.core.utility

import androidx.compose.animation.core.Easing
import org.lifetrack.ltapp.model.data.dclass.LTPreferences
import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.model.data.dclass.LtSettings
import org.lifetrack.ltapp.model.data.dclass.ProfileInfo
import org.lifetrack.ltapp.model.data.dclass.SignUpInfo
import org.lifetrack.ltapp.model.data.dclass.UserPreferences
import org.lifetrack.ltapp.model.data.dto.LoginRequest
import org.lifetrack.ltapp.model.data.dto.Message
import org.lifetrack.ltapp.model.data.dto.SignUpRequest
import org.lifetrack.ltapp.model.data.dto.UserDataResponse
import org.lifetrack.ltapp.model.roomdb.MessageEntity


fun Message.toEntity(): MessageEntity{
    return MessageEntity(
        id = this.id,
        text = this.text,
        isFromPatient = this.isFromPatient,
        timestamp = this.timestamp,
        type = this.type,
        chatId = this.chatId,
    )
}

fun MessageEntity.toDto() = Message(
    id = this.id,
    text = this.text,
    isFromPatient = this.isFromPatient,
    timestamp = this.timestamp,
    type = this.type,
    chatId = this.chatId
)

fun LoginInfo.toLoginRequest(): LoginRequest{
    return LoginRequest(
        emailAddress = this.emailAddress,
        password = this.password
    )
}

fun SignUpInfo.toSignUpRequest(): SignUpRequest{
    return SignUpRequest(
        fullName = this.fullName,
        userName = this.userName,
        password = this.password,
        emailAddress = this.emailAddress,
        phoneNumber = this.phoneNumber.toLong(),
    )
}

fun UserDataResponse.toUserProfileInformation(): ProfileInfo{
    val displayName = this.fullName ?: "N/A"
    return ProfileInfo(
        userName = this.userName.replaceFirstChar { it.uppercase() },
        userEmail = this.emailAddress,
        userFullName = displayName,
        userPhoneNumber = this.phoneNumber.toString(),
        userInitials = ZetuZetuUtil.generateInitials(displayName),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun ProfileInfo.toUserPreferences(): UserPreferences{
    return UserPreferences(
        userEmail = this.userEmail,
        userName = this.userName,
        userInitials = this.userInitials,
        userFullName = this.userFullName,
        userPhoneNumber = this.userPhoneNumber,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        lastLoginAt = this.lastLoginAt
    )
}

fun UserPreferences.toProfileInfo(): ProfileInfo {
    return ProfileInfo(
        userName = this.userName.ifBlank { "Loading ..." },
        userEmail = this.userEmail,
        userFullName = this.userFullName.ifBlank { "Loading ..." },
        userInitials = this.userInitials,
        userPhoneNumber = this.userPhoneNumber.ifBlank { "Loading ..." },
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        lastLoginAt = this.lastLoginAt
    )
}

fun LTPreferences.toLtSettings(): LtSettings {
    return LtSettings(
        notifications = this.appNotificationsEnabled,
        emailNotifications = this.appEmailNotificationsEnabled,
        smsNotifications = this.appSmsNotificationsEnabled,
        animations = this.appAnimationsEnabled,
        dataConsent = this.userPatientDataConsentEnabled,
        reminders = this.appReminderNotificationsEnabled
    )
}

fun LtSettings.toLTPreferences(): LTPreferences {
    return LTPreferences(
        appNotificationsEnabled = this.notifications,
        appEmailNotificationsEnabled = this.emailNotifications,
        appSmsNotificationsEnabled = this.smsNotifications,
        appAnimationsEnabled = this.animations,
        userPatientDataConsentEnabled = this.dataConsent,
        appReminderNotificationsEnabled = this.reminders
    )
}

fun Easing.transform(from: Float, to: Float, value: Float): Float {
    return transform(((value - from) * (1f / (to - from))).coerceIn(0f, 1f))
}
