package org.lifetrack.ltapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.components.medicalcharts.BloodPressChart
import org.lifetrack.ltapp.ui.components.medicalcharts.InfoRow
import org.lifetrack.ltapp.ui.components.medicalcharts.LabTestItem
import org.lifetrack.ltapp.ui.components.medicalcharts.MedicalCard
import org.lifetrack.ltapp.ui.components.medicalcharts.MetricBadge
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch


@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticScreen(presenter: UserPresenter) {
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
                    IconButton(onClick = { LTNavDispatch.navigateBack() }) {
                        Icon(Icons.Default.ArrowCircleLeft, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Export */ }) {
                        Icon(Icons.Default.Refresh, "Export")
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
                    BloodPressChart(
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
                    labTests.forEach { test ->
                        LabTestItem(test)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

