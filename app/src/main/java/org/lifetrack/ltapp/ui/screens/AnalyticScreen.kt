package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import org.lifetrack.ltapp.model.LtMockData
import org.lifetrack.ltapp.ui.components.medicalcharts.BloodPressChart
import org.lifetrack.ltapp.ui.components.medicalcharts.ConditionAlertBanner
import org.lifetrack.ltapp.ui.components.medicalcharts.ECGRenderer
import org.lifetrack.ltapp.ui.components.medicalcharts.LabCorrelationGroup
import org.lifetrack.ltapp.ui.components.medicalcharts.LabHistogram
import org.lifetrack.ltapp.ui.components.medicalcharts.MPRadarChart
import org.lifetrack.ltapp.ui.components.medicalcharts.MetricBadge
import org.lifetrack.ltapp.ui.components.medicalcharts.VitalsHeatMap
import org.lifetrack.ltapp.ui.components.medicalcharts.cards.AnalyticSectionCard
import org.lifetrack.ltapp.ui.components.medicalcharts.cards.HealthCard
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticScreen() {
    val patient = LtMockData.dPatient
    val cardio = LtMockData.liveCardioVitals.last()
    val recovery = LtMockData.weeklyRecoveryTrends.first()

    val bpProducer = remember { CartesianChartModelProducer() }
    val respiratoryProducer = remember { CartesianChartModelProducer() }


    LaunchedEffect(Unit) {
        respiratoryProducer.runTransaction {
            lineSeries { series(LtMockData.respiratoryHistory.map { it.spo2Percentage }) }
        }
        bpProducer.runTransaction {
            lineSeries {
                series(LtMockData.liveCardioVitals.mapNotNull { it.systolicBP?.toDouble() })
                series(LtMockData.liveCardioVitals.mapNotNull { it.diastolicBP?.toDouble() })
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column {
                        Text(patient.name, fontWeight = FontWeight.Black)
                        Text(
                            "MRN: ${patient.id}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { LTNavDispatch.navigateBack() }) {
                        Icon(imageVector = Icons.Default.ArrowCircleLeft, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { ConditionAlertBanner(patient.condition, patient.bloodPressure) }

            item {
                AnalyticSectionCard(title = "BP FREQUENCY DISTRIBUTION") {
                    Text(
                        "Historical Reading Density (30 Days)",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(Modifier.height(16.dp))
                    LabHistogram(LtMockData.bpFrequencyDistribution)
                }
            }

            item {
                AnalyticSectionCard(title = "VITALS CORRELATION ANALYSIS") {
                    BloodPressChart(
                        systolicData = LtMockData.systolicHistory,
                        diastolicData = LtMockData.diastolicHistory
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MetricBadge(
                            value = LtMockData.dPatient.bloodPressure,
                            label = "Current",
                            isCritical = true
                        )
                        MetricBadge(value = "+12%", label = "Trend")
                        MetricBadge(value = "3 days", label = "Since Last Med")
                    }
                }
            }

            item {
                AnalyticSectionCard(title = "DIURNAL RISK HEATMAP") {
                    Text(
                        "Risk Intensity by Day & Time Block",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(Modifier.height(16.dp))
                    VitalsHeatMap(LtMockData.vitalsRiskHeatmap)
                }
            }

            item {
                AnalyticSectionCard(title = "LABORATORY INSIGHTS") {
                    LtMockData.dLabTests.forEach { test ->
                        LabCorrelationGroup(test)
                        if (test != LtMockData.dLabTests.last()) HorizontalDivider(
                            Modifier.padding(
                                vertical = 16.dp
                            )
                        )
                    }
                }
            }

            item {
                HealthCard("Recovery & Thermal", Icons.Default.Bedtime, Color(0xFF9C27B0)) {
                    Column {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            VitalMetric("Readiness", "${recovery.readinessScore}", "/100")
                            VitalMetric(
                                "Temp Offset",
                                "${if (recovery.skinTempOffset >= 0) "+" else ""}${recovery.skinTempOffset}°C",
                                "off"
                            )
                            VitalMetric("Stress", "${recovery.stressLevel}", "/10")
                        }
                        Spacer(Modifier.height(16.dp))
                        MPRadarChart(recovery)
                    }
                }
            }

            item {
                HealthCard("Cardiovascular Scan", Icons.Default.MonitorHeart, Color.Red) {
                    Column {
                        if (cardio.hasArrhythmiaDetected) AlertBadge("Arrhythmia Alert", Color.Red)
                        Text("ECG Waveform", style = MaterialTheme.typography.labelSmall)
                        ECGRenderer(cardio.ecgWaveform ?: emptyList())
                        Row(
                            Modifier.padding(top = 12.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            VitalMetric("BPM", "${cardio.heartRateBpm}", "bpm")
                            VitalMetric("HRV", "${cardio.hrvMilliseconds.toInt()}", "ms")
                            VitalMetric("BP", "${cardio.systolicBP}/${cardio.diastolicBP}", "mmHg")
                        }
                        VicoDualLine(bpProducer, Color.Red, Color(0xFFFF8A80))
                    }
                }
            }
        }
    }
}