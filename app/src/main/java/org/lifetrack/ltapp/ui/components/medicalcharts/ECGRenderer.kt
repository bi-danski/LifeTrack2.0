package org.lifetrack.ltapp.ui.components.medicalcharts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.lifetrack.ltapp.ui.theme.GradientEnd

@Composable
fun ECGRenderer(waveform: List<Double>) {
    AndroidView(

        modifier = Modifier.fillMaxWidth().height(110.dp),
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                xAxis.isEnabled = false
                axisRight.isEnabled = false
                setTouchEnabled(false)
                axisLeft.textColor = Color.White.toArgb()
            }
        },
        update = { chart ->
            val entries = waveform.takeLast(120).mapIndexed { i, v -> Entry(i.toFloat(), v.toFloat()) }
            chart.data = LineData(LineDataSet(entries, "ECG").apply {
                color = GradientEnd.toArgb()
                lineWidth = 2f
                setDrawCircles(false)
            })

            chart.invalidate()
        }
    )
}