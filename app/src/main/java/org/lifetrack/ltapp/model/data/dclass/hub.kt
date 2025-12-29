package org.lifetrack.ltapp.model.data.dclass

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.datetime.LocalDateTime
import org.lifetrack.ltapp.R
import java.time.LocalDate

data class MissionItem(
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val icon: ImageVector,
    val securityLevel: String,
    val date: String,
    val location: String,
    val dateObj: LocalDate
)

data class HealthTip(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector
)

data class LabResult(
    val testName: String,
    val currentValue: Float,
    val previousValue: Float,
    val unit: String,
    val normalRange: String
)

data class MedicalVisit(
    val id: Int,
    val date: LocalDate,
    val diagnosis: String,
    val treatment: String,
    val notes: String,
    val doctor: String,
    val hospital: String,
    val status: VisitStatus,
    val attachments: List<Attachment> = emptyList(),
    val verified: Boolean = true
)

data class LabTest(
    val name: String,
    val date: String,
    val results: Map<String, String>
)

data class EpidemicAlert(
    val id: Int,
    val title: String,
    val location: String,
    val severity: String, // "Critical", "High", "Medium", "Low"
    val date: String,
    val description: String,
    val precautions: List<String>,
    val status: String, // "Active", "Contained", "New"
    val imageUrl: String = "",
    val localImageRes: Int = R.drawable.ic_medical_placeholder
)

data class SubVisit(
    val title: String,
    val timestamp: LocalDateTime // Production standard
)

data class HospitalVisit(
    val hospitalName: String,
    val department: String,
    val subVisits: List<SubVisit>
)

data class UpcomingVisit(
    val location: String,
    val treatment: String,
    val timestamp: LocalDateTime
)

data class Appointment(
    val doctor: String,
    val date: String,
    val time: String,
    val hospital: String,
    val status: String
)

data class Prescription(
    val id: String,
    val medicationName: String,
    val dosage: String,
    val instructions: String,
    val prescribedBy: String,
    val startDate: String,
    val endDate: String,
    val status: String, // "Active", "Completed", "Refill Due"
    val refillProgress: Float = 0f // 0.0 to 1.0
)

data class Attachment(
    val id: Int,
    val name: String,
    val type: AttachmentType,
    val uri: String // Uri later
)