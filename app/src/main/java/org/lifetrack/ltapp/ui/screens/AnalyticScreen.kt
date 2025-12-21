package org.lifetrack.ltapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lifetrack.ltapp.presenter.AnalyticPresenter
import org.lifetrack.ltapp.ui.components.medicalcharts.*

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticScreen(
    navController: NavController,
    presenter: AnalyticPresenter
) {
    val patient = presenter.dummyPatient
    val bpData = presenter.dummyBpData
    val labTests = presenter.dummyLabTests

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            patient.value.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "ID: ${patient.value.id}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowCircleLeft, "Back")
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
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxSize(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                MedicalCard(title = "PATIENT SUMMARY") {
                    InfoRow(label = "Age/Gender", value = "${patient.value.age}y • ${patient.value.gender}")
                    InfoRow(label = "Blood Type", value = "A+")
                    InfoRow(label = "Allergies", value = "Penicillin")
                    InfoRow(label = "Conditions", value = patient.value.condition)
                }
            }

            item {
                MedicalCard(title = "BLOOD PRESSURE TREND") {
                    BloodPressureChart(
                        systolicData = bpData.value,
                        diastolicData = bpData.value.mapValues { it.value - 30f }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MetricBadge(value = patient.value.bloodPressure, label = "Current", isCritical = true)
                        MetricBadge(value = "+12%", label = "Trend")
                        MetricBadge(value = "3 days", label = "Since Last Med")
                    }
                }
            }

            item {
                MedicalCard(title = "LAB RESULT STATS") {
                    labTests.value.forEach { test ->
                        LabTestItem(test)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

