package org.lifetrack.ltapp.model.data

import org.lifetrack.ltapp.R
import org.lifetrack.ltapp.model.dto.Message
import java.time.LocalDate
import java.util.Date

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

val emmaPatient = Patient(
        id = "LT997654321",
        name = "Emma Johnson",
        age = 45,
        gender = "Female",
        bloodPressure = "190/120",
        lastVisit = "April 26, 2024",
        condition = "Hypertensive Crisis"
    )

val dummyLabTests = listOf(
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

val dummyPrescriptions = listOf(
    Prescription(
        medication = "Lisinopril 10mg",
        dosage = "Once daily",
        duration = "30 days",
        notes = "For blood pressure control"
    ),
    Prescription(
        medication = "Metformin 500mg",
        dosage = "Twice daily",
        duration = "90 days",
        notes = "With meals"
    )
)

val dummyMessages = mutableListOf(
    Message("1", "I've been having headaches", true, "10:30 AM"),
    Message("2", "Any fever or dizziness?", false, "10:32 AM"),
    Message("3", "No fever but some dizziness", true, "10:35 AM"),
    Message("4", "Since when have you been feeling like that", false, "10:36 AM"),
    Message("5", "Come see me at my place of work at 2:00 PM", false,"10:36 AM")

)
