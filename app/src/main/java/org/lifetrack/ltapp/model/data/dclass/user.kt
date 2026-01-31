package org.lifetrack.ltapp.model.data.dclass

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlin.time.Instant

data class Patient(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val bloodPressure: String,
    val lastVisit: String,
    val condition: String
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
    val userName: String = "Loading...",
    val userEmail: String = "",
    val userFullName: String = "Loading...",
    val userInitials: String = "",
    val userPhoneNumber: String = "Loading...",
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val lastLoginAt: Instant? = null,
    val role: String = ""
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

data class User(
    val uuid: String,
    val fullName: String,
    val emailAddress: String,
    val password: String,
    val phoneNumber: String,
    val lifetrackId: String,
    val profileImageUrl: String,
    val lastActive: String
)
