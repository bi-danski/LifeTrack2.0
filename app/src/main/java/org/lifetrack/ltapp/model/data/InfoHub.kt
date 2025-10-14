package org.lifetrack.ltapp.model.data

import androidx.compose.ui.graphics.vector.ImageVector
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
    val hospital: String
)

data class LabTest(
    val name: String,
    val date: String,
    val results: Map<String, String>
)

data class Prescription(
    val medication: String,
    val dosage: String,
    val duration: String,
    val notes: String?
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