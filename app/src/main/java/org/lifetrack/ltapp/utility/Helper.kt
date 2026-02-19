package org.lifetrack.ltapp.utility

import androidx.compose.animation.core.Easing
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.lifetrack.ltapp.model.data.dclass.Appointment
import org.lifetrack.ltapp.model.data.dclass.LTPreferences
import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.model.data.dclass.LtSettings
import org.lifetrack.ltapp.model.data.dclass.ProfileInfo
import org.lifetrack.ltapp.model.data.dclass.SignUpInfo
import org.lifetrack.ltapp.model.data.dclass.UIAppointmentStatus
import org.lifetrack.ltapp.model.data.dclass.UserPreferences
import org.lifetrack.ltapp.model.data.dto.AppointmentStatus
import org.lifetrack.ltapp.model.data.dto.AppointmentUpdate
import org.lifetrack.ltapp.model.data.dto.LoginRequest
import org.lifetrack.ltapp.model.data.dto.Message
import org.lifetrack.ltapp.model.data.dto.SignUpRequest
import org.lifetrack.ltapp.model.data.dto.UserDataResponse
import org.lifetrack.ltapp.model.roomdb.MessageEntity
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


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

fun UserDataResponse.toUserProfileInfo(): ProfileInfo{
    val displayName = this.fullName ?: "N/A"
    return ProfileInfo(
        userName = this.userName.replaceFirstChar { it.uppercase() },
        userEmail = this.emailAddress,
        userFullName = displayName,
        userPhoneNumber = this.phoneNumber.toString(),
        userInitials = ZetuZetuUtil.generateInitials(displayName),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        role = this.role
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
        lastLoginAt = this.lastLoginAt,
        userRole = this.role
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
        lastLoginAt = this.lastLoginAt,
        role = this.userRole?.lowercase()?.replaceFirstChar { it.uppercase() }
    )
}

fun LTPreferences.toLtSettings(): LtSettings {
    return LtSettings(
        notifications = this.appNotificationsEnabled,
        emailNotifications = this.appEmailNotificationsEnabled,
        smsNotifications = this.appSmsNotificationsEnabled,
        animations = this.appAnimationsEnabled,
        dataConsent = this.userPatientDataConsentEnabled,
        reminders = this.appReminderNotificationsEnabled,
        carouselAutoRotate = this.appCarouselAutoRotationEnabled,
        preferredLanguage = this.preferredLanguage
    )
}

fun LocalDateTime.customFormat(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.toJavaLocalDateTime().format(formatter)
}

fun LocalDate.toYearMonth(): YearMonth = YearMonth.from(this)

fun YearMonth.formatMonthYear(): String {
    val month = month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    return "$month $year"
}

fun Easing.transform(from: Float, to: Float, value: Float): Float {
    return transform(((value - from) * (1f / (to - from))).coerceIn(0f, 1f))
}

@OptIn(ExperimentalUuidApi::class)
fun AppointmentUpdate.toAppointment(): Appointment {
    return Appointment(
        id = this.id ?: Uuid.random().toHexString(),
        doctor = this.doctor,
        hospital = this.hospital,
        status = when (this.status) {
            AppointmentStatus.UPCOMING -> UIAppointmentStatus.UPCOMING
            AppointmentStatus.ATTENDED -> UIAppointmentStatus.ATTENDED
            AppointmentStatus.RECENTLY_BOOKED -> UIAppointmentStatus.RECENTLY_BOOKED
            AppointmentStatus.RESCHEDULED -> UIAppointmentStatus.RESCHEDULED
            AppointmentStatus.DISMISSED -> UIAppointmentStatus.DISMISSED
            AppointmentStatus.CANCELLED -> UIAppointmentStatus.CANCELLED
        },
        bookedAt = this.bookedAt,
        scheduledAt = this.scheduledAt.toKotlinLocalDateTime()
    )
}
