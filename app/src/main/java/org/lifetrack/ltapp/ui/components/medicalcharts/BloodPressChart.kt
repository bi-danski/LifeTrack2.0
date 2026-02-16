package org.lifetrack.ltapp.ui.components.medicalcharts

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import org.lifetrack.ltapp.ui.theme.BlueFulani
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun BloodPressChart(systolicData: Map<Date, Float>, diastolicData: Map<Date, Float>, themeMode: Boolean) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                isFocusable = true
                isHovered = true
                xAxis.textColor = if (isSystemInDarkTheme) Color.White.toArgb() else Color.Black.copy(0.87f).toArgb()
                axisLeft.textColor = if (isSystemInDarkTheme) Color.White.toArgb() else Color.Black.copy(0.87f).toArgb()
                description.isEnabled = false
                legend.isEnabled = true
                legend.textColor = if (isSystemInDarkTheme) Color.White.toArgb() else Color.Black.copy(0.87f).toArgb()

                val systolicEntries = systolicData.map {
                    Entry(it.key.time.toFloat(), it.value)
                }
                val diastolicEntries = diastolicData.map {
                    Entry(it.key.time.toFloat(), it.value)
                }

                data = LineData(
                    LineDataSet(systolicEntries, "Systolic").apply {
                        color = Color(0xFFE53935).toArgb()
                        lineWidth = 2.5f
                        setDrawCircles(true)
                        circleRadius = 4f
                        setDrawValues(false)
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                    },
                    LineDataSet(diastolicEntries, "Diastolic").apply {
                        color = Color(0xFF1E88E5).toArgb()
                        lineWidth = 2.5f
                        setDrawCircles(true)
                        circleRadius = 4f
                        setDrawValues(false)
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                    }
                )

                xAxis.apply {
                    setPadding(0,1, 0, 10)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 86400000f // 1 day
                    valueFormatter = object : ValueFormatter() {
                        @SuppressLint("SimpleDateFormat")
                        private val format = SimpleDateFormat("MMM dd")
                        override fun getFormattedValue(value: Float) =
                            format.format(Date(value.toLong()))
                    }
                }

                axisLeft.apply {
                    setPadding(0,0,2,0)
                    axisMinimum = 50f
                    axisMaximum = 200f
                    addLimitLine(LimitLine(140f, "Normal").apply {
                        lineColor = Color(0xFF4CAF50).toArgb()
                        textColor = if (themeMode) Color.Green.toArgb() else BlueFulani.toArgb()
                        lineWidth = 1f
                    })
                }

                axisRight.isEnabled = false
                animateX(1000)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)

    )
}

































//@Composable
//fun BloodPressureChart(
//    systolicData: Map<Date, Float>,
//    diastolicData: Map<Date, Float>,
//    modifier: Modifier = Modifier
//) {
//    val context = LocalContext.current
//
//    AndroidView(
//        factory = { context ->
//            LineChart(context).apply {
//                configureBaseChart()
//                configureBloodPressureAxes()
//
//                val systolicEntries = systolicData.map {
//                    Entry(it.key.time.toFloat(), it.value)
//                }
//                val diastolicEntries = diastolicData.map {
//                    Entry(it.key.time.toFloat(), it.value)
//                }
//
//                val systolicSet = LineDataSet(systolicEntries, "Systolic").apply {
//                    styleLineDataSet(
//                        color = EmergencyRed.toArgb(),
//                        fillColor = EmergencyRed.copy(alpha = 0.2f).toArgb()
//                    )
//                }
//
//                val diastolicSet = LineDataSet(diastolicEntries, "Diastolic").apply {
//                    styleLineDataSet(
//                        color = HospitalBlue.toArgb(),
//                        fillColor = HospitalBlue.copy(alpha = 0.2f).toArgb()
//                    )
//                }
//
//                val limitLine = LimitLine(140f, "Normal Threshold").apply {
//                    lineColor = SuccessGreen.toArgb()
//                    lineWidth = 1.5f
//                }
//                axisLeft.addLimitLine(limitLine)
//
//                data = LineData(systolicSet, diastolicSet)
//                animateX(1000)
//            }
//        },
//        modifier = modifier
//            .fillMaxWidth()
//            .height(250.dp)
//    )
//}
