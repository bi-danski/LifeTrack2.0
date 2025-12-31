package org.lifetrack.ltapp.model.data.dclass

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.util.UUID
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class User @OptIn(ExperimentalTime::class) constructor(
    var uuid: String,
    val lifetrackId: String?,
    val emailAddress: String,
    val phoneNumber: String,
    val password: String,
    val fullName: String,
    val profileImageUrl: String,
    var activated: Boolean = false,
    var lastActive: Instant,
    val updatedAt: Long = System.currentTimeMillis()
)

data class Practitioner(
    var uuid: String = UUID.randomUUID().toString(),
    val accessLevel: Int = 0,
    val hospitalId: String,
    val lifetrackId: String,
    val fullName: String = "",
    val phoneNumber: String = "",
    val emailAddress: String = "",
    var passwordHash: String = "",
    val role: String = "practitioner",
    val profileImageUrl: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

data class Patient(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val bloodPressure: String,
    val lastVisit: String,
    val condition: String
)

//@JvmOverloads
data class Kiongozi(
    var uuid: String = "",
    val fullName: String,
    val emailAddress: String,
    val lifetrackID : String = "",
    var passwordHash: String = "",
    var phoneNumber: String = ""
)

data class Hospital(
    val hospitalId: String,
    val hospitalName: String,
    val hospitalLocation: String
)

data class DoctorProfile(
    val id: Int,
    val name: String,
    val specialty: String,
    val status: String,
    val imageRes: Int,
    val experienceYears: Int,
    val availability: String,
    val rating: Float,
    val hospital: String,
    val waitTime: String
)

data class Premium(
    val id: Int,
    val title: String,
    val description: String,
    val icon: @Composable () -> Unit,
    val accentColor: Color
)

data class ProfileInfo(
    val userName: String = "",
    val userEmail: String = "",
    val userFullName: String = "",
    val userInitials: String = "",
    val userPhoneNumber: String = "",
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)

data class LoginInfo(
    val emailAddress: String = "",
    val password: String = ""
)

data class SignUpInfo(
    val fullName: String = "",
    val userName: String = "",
    val password: String = "",
    val emailAddress: String = "",
    val phoneNumber: String = "",
)
