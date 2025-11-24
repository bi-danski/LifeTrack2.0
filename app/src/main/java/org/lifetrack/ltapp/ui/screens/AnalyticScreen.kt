package org.lifetrack.ltapp.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.lifetrack.ltapp.model.data.*
import org.lifetrack.ltapp.ui.components.medicalcharts.*
import org.lifetrack.ltapp.ui.theme.LTAppTheme

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticScreen(navController: NavController) {
    val patient = remember { emmaPatient }
    val bpData = remember { bPressureData }
    val labTests = remember { dummyLabTests }
    val prescriptions = remember { dummyPrescriptions }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            patient.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "ID: ${patient.id}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Export */ }) {
                        Icon(Icons.Default.Share, "Export")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AlertCard(
                    title = "CRITICAL HYPERTENSION",
                    message = "Current BP: ${patient.bloodPressure} mmHg",
                    actions = {
                        TextButton(onClick = { /* Call */ }) {
                            Text("CALL PATIENT")
                        }
                        TextButton(onClick = { /* Acknowledge */ }) {
                            Text("ACKNOWLEDGE")
                        }
                    }
                )
            }

            item {
                MedicalCard(title = "PATIENT SUMMARY") {
                    InfoRow(label = "Age/Gender", value = "${patient.age}y • ${patient.gender}")
                    InfoRow(label = "Blood Type", value = "A+")
                    InfoRow(label = "Allergies", value = "Penicillin")
                    InfoRow(label = "Conditions", value = patient.condition)
                }
            }

            item {
                MedicalCard(title = "BLOOD PRESSURE TREND") {
                    BloodPressureChart(
                        systolicData = bpData,
                        diastolicData = bpData.mapValues { it.value - 30f }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MetricBadge(value = patient.bloodPressure, label = "Current", isCritical = true)
                        MetricBadge(value = "+12%", label = "Trend")
                        MetricBadge(value = "3 days", label = "Since Last Med")
                    }
                }
            }

            item {
                MedicalCard(title = "LAB RESULTS") {
                    labTests.forEach { test ->
                        LabTestItem(test)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            item {
                MedicalCard(title = "PRESCRIPTIONS") {
                    prescriptions.forEach { prescription ->
                        PrescriptionItem(prescription)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun PreviewAnalyticScreen(){
    LTAppTheme {
        AnalyticScreen(
            navController = rememberNavController()
        )
    }
}