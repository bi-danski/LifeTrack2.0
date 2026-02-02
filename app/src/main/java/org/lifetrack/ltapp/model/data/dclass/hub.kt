package org.lifetrack.ltapp.model.data.dclass

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.datetime.LocalDateTime
import org.lifetrack.ltapp.R
import java.time.LocalDate
import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
    val id: String = Uuid.random().toString(),
    val doctor: String,
    val dateTime: LocalDateTime,
    val hospital: String,
    val status: AppointmentStatus
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
    val refillProgress: Float = 0f // 0.0 to 1.0
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


/**
 * 1. MOVEMENT & ACTIVITY
 * Focuses on physical displacement and kinetic energy.
 */
data class ActivityMetrics(
    val timestamp: Instant,
    val stepCount: Int,
    val cadence: Int,               // Steps per minute
    val distanceMeters: Double,
    val elevationGainMeters: Double,
    val caloriesBurned: Double,
    val activeMinutes: Duration,
    val intensityLevel: Intensity   // Enum: LOW, MODERATE, VIGOROUS
)

/**
 * 2. CARDIOVASCULAR HEALTH
 * Data derived from PPG sensors and electrical heart signals.
 */
data class CardioMetrics(
    val timestamp: Instant,
    val heartRateBpm: Int,
    val hrvMilliseconds: Double,    // Heart Rate Variability
    val hasArrhythmiaDetected: Boolean,
    val ecgWaveform: List<Double>?, // Voltage samples if a scan was taken
    val systolicBP: Int?,           // Estimated Blood Pressure
    val diastolicBP: Int?
)

/**
 * 3. BLOOD & RESPIRATORY METRICS
 * Focuses on gas exchange and metabolic efficiency.
 */
data class RespiratoryMetrics(
    val timestamp: Instant,
    val spo2Percentage: Double,     // Blood Oxygen Saturation
    val vo2Max: Double?,            // Cardio Fitness score
    val breathsPerMinute: Int,
    val respiratoryEffort: Double,  // New for 2026: measuring lung strain
    val hydrationLevel: Double?     // Bio-impedance based (percentage)
)

/**
 * 4. SLEEP & RECOVERY
 * High-level interpretation of physiological readiness.
 */
data class RecoveryMetrics(
    val lastSyncTime: Instant,
    val sleepDuration: Duration,
    val sleepScore: Int,            // 0-100 scale
    val remDuration: Duration,
    val deepSleepDuration: Duration,
    val skinTempOffset: Double,     // Deviation from baseline (e.g., +0.4°C)
    val stressLevel: Int,           // 1-10 scale based on GSR and HRV
    val readinessScore: Int         // "Body Battery" or "Daily Readiness"
)

enum class Intensity { LOW, MODERATE, VIGOROUS }