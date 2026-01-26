package org.lifetrack.ltapp.ui.components.medicalcharts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.lifetrack.ltapp.ui.theme.EmergencyRed
import org.lifetrack.ltapp.ui.theme.SuccessGreen
import org.lifetrack.ltapp.ui.theme.WarningYellow


@Composable
fun MedicationAdherenceChart(adherenceData: Map<String, Float>, modifier: Modifier = Modifier) {
//    val context = LocalContext.current

    AndroidView(
        factory = { context ->
            HorizontalBarChart(context).apply {
                configureBaseChart()

                val entries = adherenceData.values.mapIndexed { index, value ->
                    BarEntry(index.toFloat(), value)
                }

                val dataSet = BarDataSet(entries, "Adherence %").apply {
                    colors = listOf(
                        SuccessGreen.toArgb(),
                        WarningYellow.toArgb(),
                        EmergencyRed.toArgb()
                    )
                    valueTextColor = Color.Black.toArgb()
                    valueTextSize = 10f
                }

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(adherenceData.keys.toList())
                    setDrawGridLines(false)
                    axisMinimum = 0f
                    axisMaximum = 100f
                }

                axisLeft.apply {
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    setDrawLabels(false)
                }

                axisRight.isEnabled = false
                data = BarData(dataSet)
                animateY(1000)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
    )
}

