package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.model.data.dclass.LabTest
import org.lifetrack.ltapp.model.data.mock.LtMockData
import org.lifetrack.ltapp.ui.components.medicalcharts.BloodPressChart
import org.lifetrack.ltapp.ui.components.medicalcharts.MetricBadge
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticScreen() {
    val patient = LtMockData.dPatient

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(patient.name, fontWeight = FontWeight.Black)
                        Text("MRN: ${patient.id} • Statistical Insights", style = MaterialTheme.typography.labelSmall)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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
                    Text("Historical Reading Density (30 Days)", style = MaterialTheme.typography.labelSmall)
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
//                    MultiVitalTrendChart(LtMockData.vitalsCorrelationData)
//                    Spacer(Modifier.height(12.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MetricBadge(value = LtMockData.dPatient.bloodPressure, label = "Current", isCritical = true)
                        MetricBadge(value = "+12%", label = "Trend")
                        MetricBadge(value = "3 days", label = "Since Last Med")
                    }
                }
//                    VitalsComparisonRow(patient.bloodPressure)
            }

            item {
                AnalyticSectionCard(title = "DIURNAL RISK HEATMAP") {
                    Text("Risk Intensity by Day & Time Block", style = MaterialTheme.typography.labelSmall)
                    Spacer(Modifier.height(16.dp))
                    VitalsHeatmap(LtMockData.vitalsRiskHeatmap)
                }
            }

            item {
                AnalyticSectionCard(title = "LABORATORY INSIGHTS") {
                    LtMockData.dLabTests.forEach { test ->
                        LabCorrelationGroup(test)
                        if (test != LtMockData.dLabTests.last()) HorizontalDivider(Modifier.padding(vertical = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun VitalsHeatmap(riskMatrix: List<List<Float>>) {
    val days = listOf("M", "T", "W", "T", "F", "S", "S")
    val times = listOf("6a", "10a", "2p", "6p", "10p", "2a")

    Column {
        Row(Modifier.fillMaxWidth().padding(start = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            times.forEach { Text(it, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp) }
        }
        riskMatrix.forEachIndexed { index, row ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(days[index], Modifier.width(20.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                row.forEach { intensity ->
                    Box(
                        Modifier.weight(1f).height(24.dp).padding(1.dp)
                            .background(MaterialTheme.colorScheme.error.copy(alpha = intensity), RoundedCornerShape(2.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun LabHistogram(distribution: Map<String, Int>) {
    val maxFreq = distribution.values.maxOrNull() ?: 1
    Row(Modifier.fillMaxWidth().height(100.dp), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.Bottom) {
        distribution.forEach { (range, freq) ->
            val heightPercent = freq.toFloat() / maxFreq
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier.fillMaxWidth().fillMaxHeight(heightPercent)
                        .background(if (heightPercent > 0.7f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                )
                Text(range, style = MaterialTheme.typography.labelSmall, fontSize = 8.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}


@Composable
fun MultiVitalTrendChart(data: Map<Date, Triple<Float, Float, Float>>) {
    val sorted = data.toList().sortedBy { it.first }
    val bpColor = MaterialTheme.colorScheme.primary
    val hrColor = Color(0xFFE74C3C)
    val spo2Color = Color(0xFF2ECC71)

    Column {
        Row(Modifier.fillMaxWidth().padding(bottom = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ChartLegend("BP", bpColor); ChartLegend("HR", hrColor); ChartLegend("SpO2", spo2Color)
        }
        Canvas(Modifier.fillMaxWidth().height(160.dp)) {
            val spacing = size.width / (sorted.size - 1).coerceAtLeast(1)
            val bpPath = Path(); val hrPath = Path()

            sorted.forEachIndexed { i, entry ->
                val x = i * spacing
                val yBp = size.height - (entry.second.first / 220f * size.height)
                val yHr = size.height - (entry.second.second / 220f * size.height)
                val ySpo2 = size.height - (entry.second.third / 110f * size.height)

                if (i == 0) { bpPath.moveTo(x, yBp); hrPath.moveTo(x, yHr) }
                else {
                    val prevX = (i - 1) * spacing
                    val prevYBp = size.height - (sorted[i-1].second.first / 220f * size.height)
                    bpPath.cubicTo((prevX + x)/2, prevYBp, (prevX + x)/2, yBp, x, yBp)
                    hrPath.lineTo(x, yHr)
                }
                drawCircle(spo2Color, 3.dp.toPx(), Offset(x, ySpo2))
            }
            drawPath(bpPath, bpColor, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
            drawPath(hrPath, hrColor, style = Stroke(2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))))
        }
    }
}


// --- COMPONENT: LAB RANGE BARS ---
@Composable
fun LabResultRangeBar(label: String, valueStr: String) {
    val isAbnormal = valueStr.contains("High") || valueStr.contains("Critical") || valueStr.contains("Low")
    val bias = when {
        valueStr.contains("Critical") -> 0.9f; valueStr.contains("High") -> 0.75f; valueStr.contains("Low") -> 0.2f; else -> 0.5f
    }
    Column(Modifier.padding(vertical = 8.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodySmall)
            Text(valueStr, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = if (isAbnormal) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
        }
        Spacer(Modifier.height(6.dp))
        Box(Modifier.fillMaxWidth().height(10.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)) {
            Box(Modifier.fillMaxWidth(0.4f).fillMaxHeight().align(Alignment.Center).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)))
            Canvas(Modifier.fillMaxSize()) { drawCircle(if (isAbnormal) Color(0xFFE74C3C) else Color(0xFF3498DB), 5.dp.toPx(), Offset(size.width * bias, size.height / 2)) }
        }
    }
}


@Composable
fun LabCorrelationGroup(test: LabTest) {
    Column {
        Text(test.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        test.results.forEach { (key, value) -> LabResultRangeBar(key, value) }
    }
}

// --- UTILITY COMPONENTS ---
@Composable
fun AnalyticSectionCard(title: String, content: @Composable () -> Unit) {
    Surface(tonalElevation = 1.dp, shape = RoundedCornerShape(24.dp), border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(16.dp)); content()
        }
    }
}

@Composable
fun ChartLegend(text: String, color: Color, isSolid: Boolean = true, isPoint: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (isPoint) Box(Modifier.size(8.dp).background(color, CircleShape)) else Box(Modifier.width(12.dp).height(3.dp).background(color))
        Text(" $text", style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun ConditionAlertBanner(condition: String, lastBp: String) {
    Surface(color = MaterialTheme.colorScheme.errorContainer, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(condition.uppercase(), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.error)
                Text("Last BP: $lastBp. Crisis detected.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun VitalsComparisonRow(currentBp: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column { Text("CURRENT", style = MaterialTheme.typography.labelSmall); Text(currentBp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.error) }
        Column(horizontalAlignment = Alignment.End) { Text("TARGET", style = MaterialTheme.typography.labelSmall); Text("< 120/80", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
    }
}