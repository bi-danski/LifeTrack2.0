package org.lifetrack.ltapp.model.data.dclass

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.datetime.LocalDateTime
import org.lifetrack.ltapp.R
import java.time.LocalDate
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi

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
    val severity: String,
    val date: String,
    val description: String,
    val precautions: List<String>,
    val status: String,
    val imageUrl: String = "",
    val localImageRes: Int = R.drawable.ic_medical_placeholder
)

data class SubVisit(
    val title: String,
    val timestamp: LocalDateTime
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

data class Appointment @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String,
    val doctor: String,
    val scheduledAt: LocalDateTime,
    val hospital: String,
    val status: UIAppointmentStatus,
    val bookedAt: Instant = Clock.System.now()
)

data class Prescription(
    val id: String,
    val medicationName: String,
    val dosage: String,
    val instructions: String,
    val prescribedBy: String,
    val startDate: String,
    val endDate: String,
    val status: String,
    val refillProgress: Float = 0f
)

data class Attachment(
    val id: Int,
    val name: String,
    val type: AttachmentType,
    val uri: String
)

data class NavigationTab (
    val label: String,
    val route: String,
    val icon: ImageVector
)

data class ActivityMetrics(
    val timestamp: Instant,
    val stepCount: Int,
    val cadence: Int,               
    val distanceMeters: Double,
    val elevationGainMeters: Double,
    val caloriesBurned: Double,
    val activeMinutes: Duration,
    val intensityLevel: Intensity   
)

data class CardioMetrics(
    val timestamp: Instant,
    val heartRateBpm: Int,
    val hrvMilliseconds: Double,    
    val hasArrhythmiaDetected: Boolean,
    val ecgWaveform: List<Double>?, 
    val systolicBP: Int?,           
    val diastolicBP: Int?
)

data class RespiratoryMetrics(
    val timestamp: Instant,
    val spo2Percentage: Double,     
    val vo2Max: Double?,            
    val breathsPerMinute: Int,
    val respiratoryEffort: Double,  
    val hydrationLevel: Double?     
)

data class RecoveryMetrics(
    val lastSyncTime: Instant,
    val sleepDuration: Duration,
    val sleepScore: Int,            
    val remDuration: Duration,
    val deepSleepDuration: Duration,
    val skinTempOffset: Double,     
    val stressLevel: Int,           
    val readinessScore: Int         
)

data class LtSettings(
    val notifications: Boolean = true,
    val emailNotifications: Boolean = false,
    val smsNotifications: Boolean = false,
    val animations: Boolean = true,
    val dataConsent: Boolean = false,
    val reminders: Boolean = true,
    val carouselAutoRotate: Boolean = true,
    val preferredLanguage: String = "en"
)

data class MenuItemData(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val rightIcon: ImageVector?,
)

data class ToggleItemData(
    val title: String,
    var icon: ImageVector,
)

data class StatusChipData(
    val label: String,
    val color: Color,
    val icon: ImageVector
)

data class LanguageOption(
    val code: String,
    val name: String,
    val nativeName: String,
    val flag: String
)