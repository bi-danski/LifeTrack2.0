package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.component.LineComponent
import org.lifetrack.ltapp.model.LtMockData
import org.lifetrack.ltapp.ui.components.medicalcharts.cards.SectionCard
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VitalScreen() {
    val respiratoryProducer = remember { CartesianChartModelProducer() }
    val bpProducer = remember { CartesianChartModelProducer() }

    val activity = LtMockData.dailyActivityHistory.last()
    val respiratory = LtMockData.respiratoryHistory.last()


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
                title = { Text("CLINICAL INTELLIGENCE", fontWeight = FontWeight.Black) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                SectionCard("Movement & Kinetics",
                    Icons.AutoMirrored.Filled.DirectionsRun, Color(0xFFFF9800)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            VitalMetric("Intensity", activity.intensityLevel.name, "")
                            VitalMetric("Cadence", "${activity.cadence}", "spm")
                            VitalMetric("Elevation", "${activity.elevationGainMeters.toInt()}", "m")
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            val distStr = "%.1f".format(Locale.US, activity.distanceMeters / 1000.0)
                            VitalMetric("Distance", distStr, "km")
                            VitalMetric("Active", "${activity.activeMinutes.inWholeMinutes}", "min")
                            VitalMetric("Burn", "${activity.caloriesBurned.toInt()}", "kcal")
                        }
                    }
                }
            }



            item {
                SectionCard("Respiratory & Metabolic", Icons.Default.Air, Color(0xFF00BCD4)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            VitalMetric("VO2 Max", "${respiratory.vo2Max ?: "--"}", "ml/kg")
                            VitalMetric("Effort", "${respiratory.respiratoryEffort}", "Idx")
                            VitalMetric("Hydration", "${respiratory.hydrationLevel ?: "--"}%", "")
                        }
                        VicoRespiratoryChart(respiratoryProducer)
                    }
                }
            }


        }
    }
}

@Composable
fun VicoDualLine(producer: CartesianChartModelProducer, c1: Color, c2: Color) {
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(Fill(c1.toArgb())),
                        stroke = LineCartesianLayer.LineStroke.Continuous(thicknessDp = 2.0f)
                    ),
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(Fill(c2.toArgb())),
                        stroke = LineCartesianLayer.LineStroke.Continuous(thicknessDp = 2.0f)
                    )
                )
            ),
            startAxis = VerticalAxis.start(),
            bottomAxis = HorizontalAxis.bottom()
        ),
        modelProducer = producer,
        modifier = Modifier.height(150.dp)
    )
}

@Composable
fun VicoRespiratoryChart(producer: CartesianChartModelProducer) {
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(Fill(Color(0xFF00BCD4).toArgb())),
                        stroke = LineCartesianLayer.LineStroke.Continuous(thicknessDp = 2.0f)
                    )
                )
            ),
            decorations = remember {
                listOf(
                    HorizontalLine(
                        y = { 95.0 },
                        line = LineComponent(fill = Fill(Color.Red.toArgb()), thicknessDp = 2f)
                    )
                )
            },
            startAxis = VerticalAxis.start(),
            bottomAxis = HorizontalAxis.bottom()
        ),
        modelProducer = producer,
        modifier = Modifier.height(140.dp)
    )
}

@Composable
fun VitalMetric(label: String, value: String, unit: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(2.dp))
            Text(unit, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun AlertBadge(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(text, color = color, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }
    }
}

