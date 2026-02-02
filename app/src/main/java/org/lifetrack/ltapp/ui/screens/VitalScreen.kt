//package org.lifetrack.ltapp.ui.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Air
//import androidx.compose.material.icons.filled.Bedtime
//import androidx.compose.material.icons.filled.DirectionsRun
//import androidx.compose.material.icons.filled.MonitorHeart
//import androidx.compose.material.icons.filled.Warning
//import androidx.compose.material3.Card
//import androidx.compose.material3.CenterAlignedTopAppBar
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import com.github.mikephil.charting.data.Entry
//import com.github.mikephil.charting.data.LineData
//import com.github.mikephil.charting.data.LineDataSet
//import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
//import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
//import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
//import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
//import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
//import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
//import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
//import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
//import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
//import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
//import com.patrykandpatrick.vico.core.common.Fill
//import com.patrykandpatrick.vico.core.common.component.LineComponent
//import org.lifetrack.ltapp.model.data.mock.LtMockData
//import com.github.mikephil.charting.charts.LineChart as MPLineChart
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun VitalScreen() {
//    val respiratoryProducer = remember { CartesianChartModelProducer() }
//    val bpProducer = remember { CartesianChartModelProducer() }
//
//    val cardio = LtMockData.liveCardioVitals.last()
//    val activity = LtMockData.dailyActivityHistory.last()
//    val respiratory = LtMockData.respiratoryHistory.last()
//    val recovery = LtMockData.weeklyRecoveryTrends.first()
//
//    LaunchedEffect(Unit) {
//        respiratoryProducer.runTransaction {
//            lineSeries { series(LtMockData.respiratoryHistory.map { it.spo2Percentage }) }
//        }
//        bpProducer.runTransaction {
//            lineSeries {
//                series(LtMockData.liveCardioVitals.mapNotNull { it.systolicBP?.toDouble() })
//                series(LtMockData.liveCardioVitals.mapNotNull { it.diastolicBP?.toDouble() })
//            }
//        }
//    }
//
//    Scaffold(
//        topBar = { CenterAlignedTopAppBar(title = { Text("CLINICAL INTELLIGENCE", fontWeight = FontWeight.Black) }) }
//    ) { padding ->
//        LazyColumn(
//            modifier = Modifier.fillMaxSize().padding(padding).background(MaterialTheme.colorScheme.background),
//            contentPadding = PaddingValues(16.dp),
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            item {
//                HealthCard("Movement & Kinetics", Icons.Default.DirectionsRun, Color(0xFFFF9800)) {
//                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                            VitalMetric("Intensity", activity.intensityLevel.name, "")
//                            VitalMetric("Cadence", "${activity.cadence}", "spm")
//                            VitalMetric("Elevation", "${activity.elevationGainMeters.toInt()}", "m")
//                        }
//                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                            VitalMetric("Distance", String.format("%.1f", activity.distanceMeters / 1000.0), "km")
//                            VitalMetric("Active", "${activity.activeMinutes.inWholeMinutes}", "min")
//                            VitalMetric("Burn", "${activity.caloriesBurned.toInt()}", "kcal")
//                        }
//                    }
//                }
//            }
//
//            item {
//                HealthCard("Cardiovascular Scan", Icons.Default.MonitorHeart, Color.Red) {
//                    Column {
//                        if (cardio.hasArrhythmiaDetected) AlertBadge("Arrhythmia Alert", Color.Red)
//                        Text("ECG Waveform", style = MaterialTheme.typography.labelSmall)
//                        ECGRenderer(cardio.ecgWaveform ?: emptyList())
//                        Row(Modifier.padding(top = 12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                            VitalMetric("BPM", "${cardio.heartRateBpm}", "bpm")
//                            VitalMetric("HRV", "${cardio.hrvMilliseconds.toInt()}", "ms")
//                            VitalMetric("BP", "${cardio.systolicBP}/${cardio.diastolicBP}", "mmHg")
//                        }
//                        VicoDualLine(bpProducer, Color.Red, Color(0xFFFF8A80))
//                    }
//                }
//            }
//
//            item {
//                HealthCard("Respiratory & Metabolic", Icons.Default.Air, Color(0xFF00BCD4)) {
//                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                            VitalMetric("VO2 Max", "${respiratory.vo2Max ?: "--"}", "ml/kg")
//                            VitalMetric("Effort", "${respiratory.respiratoryEffort}", "Idx")
//                            VitalMetric("Hydration", "${respiratory.hydrationLevel ?: "--"}%", "")
//                        }
//                        VicoRespiratoryChart(respiratoryProducer)
//                    }
//                }
//            }
//
//            item {
//                HealthCard("Recovery & Thermal", Icons.Default.Bedtime, Color(0xFF9C27B0)) {
//                    Column {
//                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                            VitalMetric("Readiness", "${recovery.readinessScore}", "/100")
//                            VitalMetric("Temp Offset", "${if(recovery.skinTempOffset >= 0) "+" else ""}${recovery.skinTempOffset}°C", "off")
//                            VitalMetric("Stress", "${recovery.stressLevel}", "/10")
//                        }
//                        Spacer(Modifier.height(16.dp))
////                        KoalaRecoverySpider(recovery)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun VicoDualLine(producer: CartesianChartModelProducer, c1: Color, c2: Color) {
//    CartesianChartHost(
//        chart = rememberCartesianChart(
//            rememberLineCartesianLayer(
//                lineProvider = LineCartesianLayer.LineProvider.series(
//                    LineCartesianLayer.rememberLine(
//                        fill = LineCartesianLayer.LineFill.single(Fill(c1.toArgb())),
//                        stroke = LineCartesianLayer.LineStroke.Continuous(thicknessDp = 2.0f)
//                    ),
//                    LineCartesianLayer.rememberLine(
//                        fill = LineCartesianLayer.LineFill.single(Fill(c2.toArgb())),
//                        stroke = LineCartesianLayer.LineStroke.Continuous(thicknessDp = 2.0f)
//                    )
//                )
//            ),
//            startAxis = VerticalAxis.start(),
//            bottomAxis = HorizontalAxis.bottom()
//        ),
//        modelProducer = producer,
//        modifier = Modifier.height(150.dp)
//    )
//}
//
//@Composable
//fun VicoRespiratoryChart(producer: CartesianChartModelProducer) {
//    CartesianChartHost(
//        chart = rememberCartesianChart(
//            rememberLineCartesianLayer(
//                lineProvider = LineCartesianLayer.LineProvider.series(
//                    LineCartesianLayer.rememberLine(
//                        fill = LineCartesianLayer.LineFill.single(Fill(Color(0xFF00BCD4).toArgb())),
//                        stroke = LineCartesianLayer.LineStroke.Continuous(thicknessDp = 2.0f)
//                    )
//                )
//            ),
//            decorations = remember {
//                listOf(
//                    HorizontalLine(
//                        y = { 95.0 },
//                        line = LineComponent(fill = Fill(Color.Red.toArgb()), thicknessDp = 2f)
//                    )
//                )
//            },
//            startAxis = VerticalAxis.start(),
//            bottomAxis = HorizontalAxis.bottom()
//        ),
//        modelProducer = producer,
//        modifier = Modifier.height(140.dp)
//    )
//}
//
//
////@OptIn(ExperimentalKoalaPlotApi::class)
////@Composable
////fun KoalaRecoverySpider(metrics: RecoveryMetrics) {
////    val categories = listOf("Readiness", "Sleep", "Stress", "Deep", "REM")
////    val radarData = remember(metrics) {
////        listOf(
////            PolarPoint(metrics.readinessScore.toFloat(), 0f),
////            PolarPoint(metrics.sleepScore.toFloat(), 72f),
////            PolarPoint((10f - metrics.stressLevel) * 10f, 144f),
////            PolarPoint((metrics.deepSleepDuration.inWholeMinutes.toFloat() / 90f) * 100f, 216f),
////            PolarPoint((metrics.remDuration.inWholeMinutes.toFloat() / 90f) * 100f, 288f)
////        )
////    }
////
////    Box(modifier = Modifier.fillMaxWidth().height(280.dp), contentAlignment = Alignment.Center) {
////
////        PolarGraph(
////            radialAxisModel = rememberFloatRadialAxisModel(listOf(0f, 20f, 40f, 60f, 80f, 100f)),
////            angularAxisModel = rememberCategoryAngularAxisModel(categories),
////            // Use named arguments for everything else to resolve the ambiguity
////            polarGraphProperties = PolarGraphProperties(
////                radialAxisGridLineStyle = LineStyle(
////                    brush = SolidColor(Color.LightGray.copy(alpha = 0.5f)),
////                    strokeWidth = 1.dp
////                ),
////                angularAxisGridLineStyle = LineStyle(
////                    brush = SolidColor(Color.LightGray.copy(alpha = 0.5f)),
////                    strokeWidth = 1.dp
////                ),
////                background = AreaStyle(brush = SolidColor(Color.Transparent)),
////                radialGridType = RadialGridType.LINES,
////                angularLabelGap = 8.dp,
////                radialLabelGap = 4.dp
////            ){
////            }
////        )
////
////        }
////
////    }
////}
//
//@Composable
//fun ECGRenderer(waveform: List<Double>) {
//    AndroidView(
//        modifier = Modifier.fillMaxWidth().height(110.dp),
//        factory = { context ->
//            MPLineChart(context).apply {
//                description.isEnabled = false
//                legend.isEnabled = false
//                xAxis.isEnabled = false
//                axisRight.isEnabled = false
//                setTouchEnabled(false)
//            }
//        },
//        update = { chart ->
//            val entries = waveform.takeLast(120).mapIndexed { i, v -> Entry(i.toFloat(), v.toFloat()) }
//            chart.data = LineData(LineDataSet(entries, "ECG").apply {
//                color = Color.Red.toArgb()
//                lineWidth = 2f
//                setDrawCircles(false)
//            })
//            chart.invalidate()
//        }
//    )
//}
//
//@Composable
//fun VitalMetric(label: String, value: String, unit: String) {
//    Column {
//        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
//        Row(verticalAlignment = Alignment.Bottom) {
//            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
//            Spacer(Modifier.width(2.dp))
//            Text(unit, style = MaterialTheme.typography.labelSmall)
//        }
//    }
//}
//
//@Composable
//fun AlertBadge(text: String, color: Color) {
//    Surface(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(bottom = 12.dp)) {
//        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
//            Icon(Icons.Default.Warning, null, tint = color, modifier = Modifier.size(16.dp))
//            Spacer(Modifier.width(6.dp))
//            Text(text, color = color, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
//        }
//    }
//}
//
//@Composable
//fun HealthCard(title: String, icon: ImageVector, color: Color, content: @Composable () -> Unit) {
//    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(icon, null, tint = color)
//                Spacer(Modifier.width(8.dp))
//                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
//            }
//            Spacer(Modifier.height(16.dp))
//            content()
//        }
//    }
//}
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
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
import org.lifetrack.ltapp.model.data.dclass.RecoveryMetrics
import org.lifetrack.ltapp.model.data.mock.LtMockData
import java.util.Locale
import com.github.mikephil.charting.charts.LineChart as MPLineChart
import com.github.mikephil.charting.charts.RadarChart as MPRadarChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VitalScreen() {
    val respiratoryProducer = remember { CartesianChartModelProducer() }
    val bpProducer = remember { CartesianChartModelProducer() }

    val cardio = LtMockData.liveCardioVitals.last()
    val activity = LtMockData.dailyActivityHistory.last()
    val respiratory = LtMockData.respiratoryHistory.last()
    val recovery = LtMockData.weeklyRecoveryTrends.first()

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
                HealthCard("Movement & Kinetics", Icons.Default.DirectionsRun, Color(0xFFFF9800)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            VitalMetric("Intensity", activity.intensityLevel.name, "")
                            VitalMetric("Cadence", "${activity.cadence}", "spm")
                            VitalMetric("Elevation", "${activity.elevationGainMeters.toInt()}", "m")
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            // Fixed Locale bug
                            val distStr = "%.1f".format(Locale.US, activity.distanceMeters / 1000.0)
                            VitalMetric("Distance", distStr, "km")
                            VitalMetric("Active", "${activity.activeMinutes.inWholeMinutes}", "min")
                            VitalMetric("Burn", "${activity.caloriesBurned.toInt()}", "kcal")
                        }
                    }
                }
            }

            item {
                HealthCard("Cardiovascular Scan", Icons.Default.MonitorHeart, Color.Red) {
                    Column {
                        if (cardio.hasArrhythmiaDetected) AlertBadge("Arrhythmia Alert", Color.Red)
                        Text("ECG Waveform", style = MaterialTheme.typography.labelSmall)
                        ECGRenderer(cardio.ecgWaveform ?: emptyList())
                        Row(Modifier.padding(top = 12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            VitalMetric("BPM", "${cardio.heartRateBpm}", "bpm")
                            VitalMetric("HRV", "${cardio.hrvMilliseconds.toInt()}", "ms")
                            VitalMetric("BP", "${cardio.systolicBP}/${cardio.diastolicBP}", "mmHg")
                        }
                        VicoDualLine(bpProducer, Color.Red, Color(0xFFFF8A80))
                    }
                }
            }

            item {
                HealthCard("Respiratory & Metabolic", Icons.Default.Air, Color(0xFF00BCD4)) {
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

            item {
                HealthCard("Recovery & Thermal", Icons.Default.Bedtime, Color(0xFF9C27B0)) {
                    Column {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            VitalMetric("Readiness", "${recovery.readinessScore}", "/100")
                            VitalMetric("Temp Offset", "${if(recovery.skinTempOffset >= 0) "+" else ""}${recovery.skinTempOffset}°C", "off")
                            VitalMetric("Stress", "${recovery.stressLevel}", "/10")
                        }
                        Spacer(Modifier.height(16.dp))
                        MPAndroidRadarChart(recovery)
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
fun MPAndroidRadarChart(metrics: RecoveryMetrics) {
    val categories = arrayOf("Readiness", "Sleep", "Relaxation", "Deep", "REM")
    val entries = remember(metrics) {
        listOf(
            RadarEntry(metrics.readinessScore.toFloat()),
            RadarEntry(metrics.sleepScore.toFloat()),
            RadarEntry((10f - metrics.stressLevel) * 10f),
            RadarEntry((metrics.deepSleepDuration.inWholeMinutes.toFloat() / 90f) * 100f),
            RadarEntry((metrics.remDuration.inWholeMinutes.toFloat() / 90f) * 100f)
        )
    }

    AndroidView(
        modifier = Modifier.fillMaxWidth().height(280.dp),
        factory = { context ->
            MPRadarChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                webColor = Color.LightGray.toArgb()
                webAlpha = 100
                webColorInner = Color.LightGray.toArgb()

                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(categories)
                    textSize = 10f
                    textColor = Color.Gray.toArgb()
                }

                yAxis.apply {
                    axisMinimum = 0f
                    axisMaximum = 100f
                    setDrawLabels(false)
                    setLabelCount(5, true)
                }
            }
        },
        update = { chart ->
            val dataSet = RadarDataSet(entries, "Recovery").apply {
                color = Color(0xFF9C27B0).toArgb()
                fillColor = Color(0xFF9C27B0).toArgb()
                setDrawFilled(true)
                fillAlpha = 60
                lineWidth = 2f
                setDrawValues(false)
            }
            chart.data = RadarData(dataSet)
            chart.animateXY(800, 800)
            chart.invalidate()
        }
    )
}

@Composable
fun ECGRenderer(waveform: List<Double>) {
    AndroidView(
        modifier = Modifier.fillMaxWidth().height(110.dp),
        factory = { context ->
            MPLineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                xAxis.isEnabled = false
                axisRight.isEnabled = false
                setTouchEnabled(false)
            }
        },
        update = { chart ->
            val entries = waveform.takeLast(120).mapIndexed { i, v -> Entry(i.toFloat(), v.toFloat()) }
            chart.data = LineData(LineDataSet(entries, "ECG").apply {
                color = Color.Red.toArgb()
                lineWidth = 2f
                setDrawCircles(false)
            })
            chart.invalidate()
        }
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

@Composable
fun HealthCard(title: String, icon: ImageVector, color: Color, content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = color)
                Spacer(Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}