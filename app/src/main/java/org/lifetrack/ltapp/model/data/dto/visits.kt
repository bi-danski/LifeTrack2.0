package org.lifetrack.ltapp.model.data.dto

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate
import kotlin.time.Instant


@Serializable
data class MongoId(
    val timestamp: Int,
    val date: Long? = null,
    val author: String? = null,
    val machineIdentifier: Int? = null,
    val processIdentifier: Short? = null,
    val counter: Int? = null
)

@Serializable
data class Visit(
    val id: MongoId,
    val date: LocalDate,
    val visitAt: Instant,
    val hospital: String,
    val department: String,
    val doctor: String,
    val reasonForVisit: String,
    val status: VisitStatus,
    val visitType: VisitType,
    val diagnosis: List<Diagnosis> = emptyList(),
    val prescriptions: List<Prescription> = emptyList(),
    val labResults: List<LabResult> = emptyList(),
    val subVisits: List<SubVisit> = emptyList(),
    val attachments: List<Attachment> = emptyList(),
    val notes: String? = null
)

@Serializable
data class Diagnosis(
    val id: String,
    val condition: String,
    val description: String,
    val severity: ConditionSeverity,
    val status: ConditionStatus,
    val icd10Code: String? = null,
    val diagnosedAt: Instant
)

@Serializable
data class Prescription(
    val id: String,
    val drugName: String,
    val drugCategory: String? = null,
    val dosage: String,
    val frequency: String,
    val duration: String? = null,
    val status: PrescriptionStatus,
    val prescribedAt: Instant,
    val notes: String? = null
)

@Serializable
data class LabResult(
    val id: String,
    val testName: String,
    val numericValue: Double? = null,
    val textResult: String? = null,
    val unit: String? = null,
    val referenceRange: String,
    val observationFlag: ObservationFlag,
    val testedAt: Instant,
    val notes: String? = null
)

@Serializable
data class SubVisit(val title: String, val timestamp: Instant)

@Serializable
data class Attachment(val id: Int, val name: String, val type: String, val uri: String)

@Serializable enum class VisitStatus { COMPLETED, ONGOING, FOLLOW_UP, CANCELLED, DISMISSED, RESCHEDULED }
@Serializable enum class VisitType { ROUTINE, EMERGENCY, FOLLOW_UP, TELEHEALTH, SPECIALIST }
@Serializable enum class ConditionSeverity { ASYMPTOMATIC, STABLE, MODERATE, SEVERE, CRITICAL }
@Serializable enum class ConditionStatus { ACTIVE, RECURRING, REMISSION, RESOLVED }
@Serializable enum class PrescriptionStatus { ACTIVE, COMPLETED, DISCONTINUED, ON_HOLD }
@Serializable enum class ObservationFlag { NORMAL, LOW, HIGH, CRITICAL_LOW, CRITICAL_HIGH, ABNORMAL }