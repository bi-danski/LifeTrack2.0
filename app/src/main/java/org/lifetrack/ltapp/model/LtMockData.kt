package org.lifetrack.ltapp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import kotlinx.datetime.LocalDateTime
import org.lifetrack.ltapp.model.data.dclass.ActivityMetrics
import org.lifetrack.ltapp.model.data.dclass.Appointment
import org.lifetrack.ltapp.model.data.dclass.Attachment
import org.lifetrack.ltapp.model.data.dclass.AttachmentType
import org.lifetrack.ltapp.model.data.dclass.CardioMetrics
import org.lifetrack.ltapp.model.data.dclass.DoctorProfile
import org.lifetrack.ltapp.model.data.dclass.EpidemicAlert
import org.lifetrack.ltapp.model.data.dclass.HospitalVisit
import org.lifetrack.ltapp.model.data.dclass.Intensity
import org.lifetrack.ltapp.model.data.dclass.LabTest
import org.lifetrack.ltapp.model.data.dclass.MedicalVisit
import org.lifetrack.ltapp.model.data.dclass.Patient
import org.lifetrack.ltapp.model.data.dclass.Premium
import org.lifetrack.ltapp.model.data.dclass.Prescription
import org.lifetrack.ltapp.model.data.dclass.RecoveryMetrics
import org.lifetrack.ltapp.model.data.dclass.RespiratoryMetrics
import org.lifetrack.ltapp.model.data.dclass.SubVisit
import org.lifetrack.ltapp.model.data.dclass.UIAppointmentStatus
import org.lifetrack.ltapp.model.data.dclass.UpcomingVisit
import org.lifetrack.ltapp.model.data.dclass.VisitStatus
import org.lifetrack.ltapp.ui.theme.PremiumGold
import org.lifetrack.ltapp.ui.theme.PremiumPurple
import org.lifetrack.ltapp.ui.theme.PremiumTeal
import java.time.LocalDate
import java.util.Date
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object LtMockData {
    val allHospitalVisits = listOf(
        HospitalVisit(
            hospitalName = "Mama Lucy Kibaki Hospital",
            department = "Oncology",
            subVisits = listOf(
                SubVisit("Chemotherapy", LocalDateTime(2025, 10, 12, 10, 0)),
                SubVisit("Physiotherapy", LocalDateTime(2025, 11, 10, 14, 30))
            )
        ),
        HospitalVisit(
            hospitalName = "Mbagathi County Referral",
            department = "Infectious Diseases",
            subVisits = listOf(
                SubVisit("Lab Results Review", LocalDateTime(2025, 12, 1, 9, 0))
            )
        ),
        HospitalVisit(
            hospitalName = "Metropolitan Hospital",
            department = "Cardiology",
            subVisits = listOf(
                SubVisit("ECG Scan", LocalDateTime(2025, 11, 20, 11, 0)),
                SubVisit("Consultation", LocalDateTime(2025, 11, 22, 10, 30))
            )
        ),
        HospitalVisit(
            hospitalName = "Avenue Hospital",
            department = "General Surgery",
            subVisits = listOf(
                SubVisit("Post-Op Checkup", LocalDateTime(2025, 12, 5, 8, 15))
            )
        )
    )

    val allMedicalVisits = listOf(
        MedicalVisit(
            id = 1,
            date = LocalDate.of(2025, 6, 15),
            diagnosis = "Upper Respiratory Infection",
            treatment = "Antibiotics (Amoxicillin), Rest",
            notes = "Follow-up recommended in 2 weeks",
            doctor = "Dr. Hilary Otieno",
            hospital = "Nakuru General Hospital",
            status = VisitStatus.COMPLETED,
            attachments = listOf(
                Attachment(
                    id = 1,
                    name = "Lab Report.pdf",
                    type = AttachmentType.LAB_RESULT,
                    uri = "uri://labreport1"
                ),
                Attachment(
                    id = 2,
                    name = "X-Ray.png",
                    type = AttachmentType.IMAGE,
                    uri = "uri://xray1"
                )
            )
        ),
        MedicalVisit(
            id = 2,
            date = LocalDate.of(2025, 7, 10),
            diagnosis = "Mild Hypertension",
            treatment = "Lifestyle changes, Monitor BP",
            notes = "Referral to cardiologist pending",
            doctor = "Dr. Mercy Baraka",
            hospital = "Rift Valley Provincial Hospital",
            status = VisitStatus.ONGOING,
            attachments = emptyList()
        ),
        MedicalVisit(
            id = 3,
            date = LocalDate.of(2025, 7, 20),
            diagnosis = "Allergic Rhinitis",
            treatment = "Antihistamines, Avoid allergens",
            notes = "Symptoms improved, continue treatment",
            doctor = "Dr. Tabitha Kerry",
            hospital = "Kabarak Mission Hospital",
            status = VisitStatus.COMPLETED,
            attachments = listOf(
                Attachment(
                    id = 3,
                    name = "Allergy Test.pdf",
                    type = AttachmentType.PDF,
                    uri = "uri://allergytest"
                )
            )
        ),
        MedicalVisit(
            id = 4,
            date = LocalDate.of(2025, 7, 25),
            diagnosis = "Vitamin D Deficiency",
            treatment = "Supplements, Sun Exposure",
            notes = "Revisit in one month",
            doctor = "Dr. Hilary Otieno",
            hospital = "Nakuru General Hospital",
            status = VisitStatus.FOLLOW_UP,
            attachments = emptyList()
        ),
        MedicalVisit(
            id = 5,
            date = LocalDate.of(2025, 7, 28),
            diagnosis = "Minor Laceration",
            treatment = "Stitches, Antibiotics",
            notes = "Keep wound clean, follow-up if infected",
            doctor = "Dr. Mercy Baraka",
            hospital = "Rift Valley Provincial Hospital",
            status = VisitStatus.COMPLETED,
            attachments = listOf(
                Attachment(
                    id = 4,
                    name = "Wound Photo.png",
                    type = AttachmentType.IMAGE,
                    uri = "uri://woundphoto"
                )
            )
        )
    )

    @OptIn(ExperimentalUuidApi::class)
    val dummyAppointments = listOf(
        Appointment(
            id = Uuid.random().toHexString(),
            doctor = "Dr. Anya Sharma",
            scheduledAt = LocalDateTime(2025, 12, 29, 10, 0),
            hospital = "Nairobi West Hospital",
            status = UIAppointmentStatus.UPCOMING

        ),
        Appointment(
            id = Uuid.random().toHexString(),
            doctor = "Dr. Ben Carter",
            scheduledAt = LocalDateTime(2025, 12, 28, 14, 30),
            hospital = "Mama Lucy Kibaki",
            status = UIAppointmentStatus.UPCOMING
        ),
        Appointment(
            id = Uuid.random().toHexString(),
            doctor = "Dr. Hilary Otieno",
            scheduledAt = LocalDateTime(2025, 12, 30, 9, 0),
            hospital = "Nakuru General",
            status = UIAppointmentStatus.ATTENDED
        ),
        Appointment(
            doctor = "Dr. Tabitha Kerry",
            id = Uuid.random().toHexString(),
            scheduledAt = LocalDateTime(2025, 12, 15, 11, 30),
            hospital = "Kabarak Mission",
            status = UIAppointmentStatus.DISMISSED
        ),
        Appointment(
            doctor = "Dr. James Mwangi",
            scheduledAt = LocalDateTime(2026, 1, 5, 14, 0),
            hospital = "Nairobi City",
            id = Uuid.random().toHexString(),
            status = UIAppointmentStatus.RESCHEDULED
        )
    )

    val epidemicAlerts = listOf(
        EpidemicAlert(
            id = 1,
            title = "Malaria Outbreak",
            location = "Nairobi County",
            severity = "High",
            date = "15 Nov - 30 Dec 2023",
            description = "Increased cases reported due to heavy rainfall and mosquito breeding",
            precautions = listOf(
                "Use mosquito nets",
                "Apply insect repellent",
                "Seek immediate treatment for symptoms"
            ),
            status = "Active",
            localImageRes = org.lifetrack.ltapp.R.drawable.malaria
        ),
        EpidemicAlert(
            id = 2,
            title = "Cholera Alert",
            location = "Coastal Region",
            severity = "Critical",
            date = "1 Dec - Present",
            description = "Waterborne outbreak detected in informal settlements",
            precautions = listOf(
                "Drink boiled or treated water",
                "Maintain proper sanitation",
                "Oral rehydration for symptoms"
            ),
            status = "New",
            localImageRes = org.lifetrack.ltapp.R.drawable.who
        ),
        EpidemicAlert(
            id = 3,
            title = "COVID-19 Variant",
            location = "Nationwide",
            severity = "Medium",
            date = "Ongoing",
            description = "New variant detected with increased transmissibility",
            precautions = listOf(
                "Get booster shots",
                "Wear masks in crowds",
                "Monitor for symptoms"
            ),
            status = "Active",
            localImageRes = org.lifetrack.ltapp.R.drawable.covid
        ),
        EpidemicAlert(
            id = 4,
            title = "General Health Alert",
            location = "Multiple Regions",
            severity = "Low",
            date = "Ongoing",
            description = "Seasonal health advisory for common illnesses",
            precautions = listOf(
                "Practice good hygiene",
                "Stay hydrated",
                "Visit health centers if symptoms persist"
            ),
            status = "Contained",
            localImageRes = org.lifetrack.ltapp.R.drawable.alerts
        )
    )

    val dummyDoctors = listOf(
        DoctorProfile(
            id = 1,
            name = "Dr. Hilary Otieno",
            specialty = "General Practitioner",
            status = "Available",
            imageRes = android.R.drawable.ic_menu_gallery,
            experienceYears = 8,
            availability = "9:00 AM - 1:00 PM",
            rating = 4.7f,
            hospital = "Nakuru General Hospital",
            waitTime = "5-10 mins"
        ),
        DoctorProfile(
            id = 2,
            name = "Dr. Mercy Baraka",
            specialty = "Cardiologist",
            status = "Busy",
            imageRes = android.R.drawable.ic_menu_gallery,
            experienceYears = 12,
            availability = "1:00 PM - 5:00 PM",
            rating = 4.9f,
            hospital = "Rift Valley Provincial Hospital",
            waitTime = "15-20 mins"
        ),
        DoctorProfile(
            id = 3,
            name = "Dr. Tabitha Kerry",
            specialty = "Allergist",
            status = "Available",
            imageRes = android.R.drawable.ic_menu_gallery,
            experienceYears = 6,
            availability = "10:00 AM - 2:00 PM",
            rating = 4.5f,
            hospital = "Kabarak Mission Hospital",
            waitTime = "10-15 mins"
        ),
        DoctorProfile(
            id = 4,
            name = "Dr. James Mwangi",
            specialty = "Pediatrician",
            status = "Available",
            imageRes = android.R.drawable.ic_menu_gallery,
            experienceYears = 10,
            availability = "9:00 AM - 12:00 PM",
            rating = 4.6f,
            hospital = "Nairobi City Hospital",
            waitTime = "5-10 mins"
        ),
        DoctorProfile(
            id = 5,
            name = "Dr. Amina Hassan",
            specialty = "Dermatologist",
            status = "Busy",
            imageRes = android.R.drawable.ic_menu_gallery,
            experienceYears = 7,
            availability = "2:00 PM - 6:00 PM",
            rating = 4.8f,
            hospital = "Mombasa Medical Center",
            waitTime = "20-25 mins"
        ),
        DoctorProfile(
            id = 6,
            name = "Dr. Mitchell Akinyi",
            specialty = "Neurologist",
            status = "Available",
            imageRes = android.R.drawable.ic_menu_gallery,
            experienceYears = 9,
            availability = "8:00 AM - 12:00 PM",
            rating = 4.6f,
            hospital = "Kisumu Referral Hospital",
            waitTime = "10-15 mins"
        ),
        DoctorProfile(
            id = 7,
            name = "Dr. Kingsley Coman",
            specialty = "Orthopedist",
            status = "Busy",
            imageRes = android.R.drawable.ic_menu_gallery,
            experienceYears = 11,
            availability = "1:00 PM - 4:00 PM",
            rating = 4.7f,
            hospital = "Eldoret Teaching Hospital",
            waitTime = "15-20 mins"
        ),
        DoctorProfile(
            id = 8,
            name = "Dr. Emmanuel Mutubi",
            specialty = "Endocrinologist",
            status = "Available",
            imageRes = android.R.drawable.ic_menu_gallery,
            experienceYears = 5,
            availability = "10:00 AM - 3:00 PM",
            rating = 4.4f,
            hospital = "Thika Level 5 Hospital",
            waitTime = "5-10 mins"
        ),
        DoctorProfile(
            id = 9,
            name = "Dr. Curtis Roy",
            specialty = "Ophthalmologist",
            status = "Available",
            imageRes = android.R.drawable.ic_menu_gallery,
            experienceYears = 13,
            availability = "2:00 PM - 6:00 PM",
            rating = 4.9f,
            hospital = "Kenyatta National Hospital",
            waitTime = "20-25 mins"
        )
    )

    val dummyPremiums = listOf(
        Premium(
            id = 1,
            title = "Priority Access",
            description = "Skip the queue with immediate consultation slots",
            icon = { Icon(Icons.Filled.FlashOn, contentDescription = null, tint = PremiumTeal) },
            accentColor = PremiumTeal
        ),
        Premium(
            id = 2,
            title = "Extended Sessions",
            description = "30-minute consultations with in-depth care",
            icon = { Icon(Icons.Filled.Schedule, contentDescription = null, tint = PremiumGold) },
            accentColor = PremiumGold
        ),
        Premium(
            id = 3,
            title = "Specialist Priority",
            description = "Access top-rated specialists first",
            icon = { Icon(Icons.Filled.Star, contentDescription = null, tint = PremiumPurple) },
            accentColor = PremiumPurple
        ),
        Premium(
            id = 4,
            title = "Personal Health Insights",
            description = "Get detailed health analytics and trends",
            icon = { Icon(Icons.Filled.Insights, contentDescription = null, tint = PremiumTeal) },
            accentColor = PremiumTeal
        )
    )


    val dPatient = Patient(
        id = "LT997654321",
        name = "Dr. Najma",
        age = 45,
        gender = "Female",
        bloodPressure = "145",
        lastVisit = "April 26, 2024",
        condition = "Hypertensive Crisis" 
    )

    val dLabTests = listOf(
        LabTest(
            name = "Metabolic Panel",
            date = "Apr 20, 2024",
            results = mapOf(
                "Glucose (Fasting)" to "126 (High)",
                "Creatinine" to "1.2 (Normal)",
                "eGFR" to "88 (Normal)",
                "Sodium" to "138 (Normal)"
            )
        ),
        LabTest(
            name = "Lipid Panel",
            date = "Apr 15, 2024",
            results = mapOf(
                "Total Cholesterol" to "240 (High)",
                "LDL Cholesterol" to "162 (Critical)",
                "HDL Cholesterol" to "38 (Low)",
                "Triglycerides" to "155 (Borderline)"
            )
        )
    )

    val dPrescriptions = listOf(
        Prescription(
            id = "RX-8801",
            medicationName = "Lisinopril 20mg",
            dosage = "1 tablet, daily",
            instructions = "Take in the morning. Do not skip doses.",
            prescribedBy = "Dr. James Mwangi",
            startDate = "Dec 18, 2025",
            endDate = "Jan 18, 2026",
            status = "Active",
            refillProgress = 0.85f
        ),
        Prescription(
            id = "RX-4402",
            medicationName = "Metformin 500mg",
            dosage = "1 tablet, twice daily",
            instructions = "Take with meals to reduce GI upset.",
            prescribedBy = "Dr. Anya Sharma",
            startDate = "Nov 15, 2025",
            endDate = "Dec 22, 2025",
            status = "Refill Due", 
            refillProgress = 0.95f
        ),
        Prescription(
            id = "RX-9122",
            medicationName = "Atorvastatin 40mg",
            dosage = "1 tablet nightly",
            instructions = "Oral. Avoid grapefruit juice.",
            prescribedBy = "Dr. Mercy Baraka",
            startDate = "Dec 01, 2025",
            endDate = "Jun 01, 2026",
            status = "Active",
            refillProgress = 0.42f
        )
    )

    val upcomingData = listOf(
        UpcomingVisit(
            location = "Mama Lucy Kibaki",
            treatment = "Cardiology Follow-up",
            timestamp = LocalDateTime(2025, 12, 28, 10, 0)
        ),
        UpcomingVisit(
            location = "Metropolitan Hospital",
            treatment = "HbA1c Lab Test",
            timestamp = LocalDateTime(2025, 12, 30, 14, 0)
        )
    )

    val vitalsRiskHeatmap = List(7) { List(6) { (0..100).random() / 100f } }

    val bpFrequencyDistribution = mapOf(
        "110-120" to 2,
        "120-130" to 5,
        "130-140" to 12,
        "140-150" to 8,
        "150-160" to 4,
        "160+" to 3
    )

    val systolicHistory = sortedMapOf(
        Date(System.currentTimeMillis() - 4 * 86400000L) to 145f,
        Date(System.currentTimeMillis() - 3 * 86400000L) to 170f,
        Date(System.currentTimeMillis() - 2 * 86400000L) to 195f,
        Date() to 190f
    )

    val diastolicHistory = sortedMapOf(
        Date(System.currentTimeMillis() - 4 * 86400000L) to 90f,
        Date(System.currentTimeMillis() - 3 * 86400000L) to 105f,
        Date(System.currentTimeMillis() - 2 * 86400000L) to 125f,
        Date() to 120f
    )

    val dailyActivityHistory = List(7) { dayOffset ->
        val timestamp = Clock.System.now().minus(dayOffset.days)
        ActivityMetrics(
            timestamp = timestamp,
            stepCount = (4000..12000).random(),
            cadence = (90..115).random(),
            distanceMeters = Random.nextDouble(3000.0, 8500.0),
            elevationGainMeters = Random.nextDouble(5.0, 40.0),
            caloriesBurned = Random.nextDouble(1800.0, 2600.0),
            activeMinutes = (20..100).random().minutes,
            intensityLevel = if (dayOffset % 3 == 0) Intensity.VIGOROUS else Intensity.MODERATE
        )
    }

    val liveCardioVitals = List(24) { hourOffset ->
        val timestamp = Clock.System.now().minus(hourOffset.hours)
        CardioMetrics(
            timestamp = timestamp,
            heartRateBpm = (75..95).random(),
            hrvMilliseconds = Random.nextDouble(30.0,55.0),
            hasArrhythmiaDetected = false,
            ecgWaveform = listOf(27.0, 33.3, 29.5, 40.49, 30.55, 47.0, 35.67, 49.49 ),
            systolicBP = if (hourOffset < 5) 190 else 145,
            diastolicBP = if (hourOffset < 5) 120 else 90
        )
    }

    val respiratoryHistory = List(7) { dayOffset ->
        val timestamp = Clock.System.now().minus(dayOffset.days)
        RespiratoryMetrics(
            timestamp = timestamp,
            spo2Percentage = Random.nextDouble(94.0, 99.0),
            vo2Max = 42.5,
            breathsPerMinute = (14..20).random(),
            respiratoryEffort = Random.nextDouble(15.0, 45.0),
            hydrationLevel = Random.nextDouble(60.0, 80.0)
        )
    }

    val weeklyRecoveryTrends = List(7) { dayOffset ->
        val syncTime = Clock.System.now().minus(dayOffset.days)
        RecoveryMetrics(
            lastSyncTime = syncTime,
            sleepDuration = (6..8).random().hours,
            sleepScore = (60..88).random(),
            remDuration = (60..110).random().minutes,
            deepSleepDuration = (40..90).random().minutes,
            skinTempOffset = kotlin.math.truncate(Random.nextDouble(-0.5, 0.5)),
            stressLevel = (6..9).random(),
            readinessScore = (40..75).random()
        )
    }
}