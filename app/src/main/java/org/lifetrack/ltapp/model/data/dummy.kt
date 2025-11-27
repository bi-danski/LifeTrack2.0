package org.lifetrack.ltapp.model.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import org.lifetrack.ltapp.R
import org.lifetrack.ltapp.model.data.dclass.DoctorProfile
import org.lifetrack.ltapp.model.data.dclass.EpidemicAlert
import org.lifetrack.ltapp.model.data.dclass.LabTest
import org.lifetrack.ltapp.model.data.dclass.MedicalVisit
import org.lifetrack.ltapp.model.data.dclass.Patient
import org.lifetrack.ltapp.model.data.dclass.Premium
import org.lifetrack.ltapp.model.data.dclass.Prescription
import org.lifetrack.ltapp.model.data.dto.Message
import org.lifetrack.ltapp.ui.theme.PremiumGold
import org.lifetrack.ltapp.ui.theme.PremiumPurple
import org.lifetrack.ltapp.ui.theme.PremiumTeal
import java.time.LocalDate
import java.util.Date
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
        localImageRes = R.drawable.malaria
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
        localImageRes = R.drawable.who
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
        localImageRes = R.drawable.covid
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
        localImageRes = R.drawable.alerts
    )
)

val medicalVisits = listOf(
    MedicalVisit(
        id = 1,
        date = LocalDate.of(2025, 6, 15),
        diagnosis = "Upper Respiratory Infection",
        treatment = "Antibiotics (Amoxicillin), Rest",
        notes = "Follow-up recommended in 2 weeks",
        doctor = "Hilary Otieno",
        hospital = "Nakuru General Hospital"
    ),
    MedicalVisit(
        id = 2,
        date = LocalDate.of(2025, 7, 10),
        diagnosis = "Mild Hypertension",
        treatment = "Lifestyle changes, Monitor BP",
        notes = "Referral to cardiologist pending",
        doctor = "Mercy Baraka",
        hospital = "Rift Valley Provincial Hospital"
    ),
    MedicalVisit(
        id = 3,
        date = LocalDate.of(2025, 7, 20),
        diagnosis = "Allergic Rhinitis",
        treatment = "Antihistamines, Avoid allergens",
        notes = "Symptoms improved, continue treatment",
        doctor = "Tabitha Kerry",
        hospital = "Kabarak Mission Hospital"
    ),
    MedicalVisit(
        id = 4,
        date = LocalDate.of(2025, 7, 25),
        diagnosis = "Vitamin D Deficiency",
        treatment = "Supplements, Sun Exposure",
        notes = "Revisit in one month",
        doctor = "Hilary Otieno",
        hospital = "Nakuru General Hospital"
    ),
    MedicalVisit(
        id = 5,
        date = LocalDate.of(2025, 7, 28),
        diagnosis = "Minor Laceration",
        treatment = "Stitches, Antibiotics",
        notes = "Keep wound clean, follow-up if infected",
        doctor = "Mercy Baraka",
        hospital = "Rift Valley Provincial Hospital"
    )
)

val bPressureData = sortedMapOf(
        Date(System.currentTimeMillis() - 6 * 86400000L) to 150f,
        Date(System.currentTimeMillis() - 5 * 86400000L) to 145f,
        Date(System.currentTimeMillis() - 4 * 86400000L) to 160f,
        Date(System.currentTimeMillis() - 3 * 86400000L) to 170f,
        Date(System.currentTimeMillis() - 2 * 86400000L) to 190f,
        Date(System.currentTimeMillis() - 86400000L) to 185f,
        Date() to 180f
    )

val dPatient = Patient(
    id = "LT997654321",
    name = "Dr. Najma",
    age = 45,
    gender = "Female",
    bloodPressure = "190/120",
    lastVisit = "April 26, 2024",
    condition = "Hypertensive Crisis"
)

val dLabTests = listOf(
    LabTest(
        name = "Complete Blood Count",
        date = "Apr 20, 2024",
        results = mapOf(
            "WBC" to "6.5 (Normal)",
            "RBC" to "4.2 (Normal)",
            "Hemoglobin" to "12.8 (Low)"
        )
    ),
    LabTest(
        name = "Lipid Panel",
        date = "Apr 15, 2024",
        results = mapOf(
            "Cholesterol" to "210 (High)",
            "Triglycerides" to "150 (Borderline)"
        )
    )
)

@OptIn(ExperimentalUuidApi::class)
val dPrescriptions = mutableListOf(
    Prescription(
        id = Uuid.random().toHexString(),
        medication = "Lisinopril 10mg",
        dosage = "Once daily",
        duration = "30 days",
        notes = "For blood pressure control"
    ),
    Prescription(
        id = Uuid.random().toHexString(),
        medication = "Metformin 500mg",
        dosage = "Twice daily",
        duration = "90 days",
        notes = "With meals"
    )
)

val dummyMessages = mutableListOf(
    Message( "I've been having headaches", true, "10:30 AM"),
    Message( "Any fever or dizziness?", false, "10:32 AM"),
    Message("No fever but some dizziness", true, "10:35 AM"),
    Message( "Since when have you been feeling like that", false, "10:36 AM"),
    Message( "Come see me at my place of work at 2:00 PM", false,"10:36 AM"),
    Message( "Okay, i'll be there on time", true,"10:39 AM")
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
