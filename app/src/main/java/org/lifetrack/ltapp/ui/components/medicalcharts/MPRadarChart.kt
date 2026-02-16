package org.lifetrack.ltapp.ui.components.medicalcharts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.lifetrack.ltapp.model.data.dclass.RecoveryMetrics

@Composable
fun MPRadarChart(metrics: RecoveryMetrics) {
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
            RadarChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                webColor = Color.LightGray.toArgb()
                webAlpha = 100
                webColorInner = Color.LightGray.toArgb()


                xAxis.apply {
                    valueFormatter = IndexAxisValueFormatter(categories)
                    textSize = 10f
                    textColor = Color.White.toArgb()
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


