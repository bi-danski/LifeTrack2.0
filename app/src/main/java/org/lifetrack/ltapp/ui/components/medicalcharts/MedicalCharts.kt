package org.lifetrack.ltapp.ui.components.medicalcharts

import android.annotation.SuppressLint
import android.graphics.Typeface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@SuppressLint("ConstantLocale")
val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
@SuppressLint("ConstantLocale")
val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())

fun LineChart.configureBaseChart() {
    description.isEnabled = false
    legend.isEnabled = true
    setTouchEnabled(true)
    isDragEnabled = true
    setScaleEnabled(true)
    setPinchZoom(true)
    setDrawGridBackground(false)
    legend.textColor = Color.Black.toArgb()
}

fun HorizontalBarChart.configureBaseChart() {
    description.isEnabled = false
    legend.isEnabled = true
    setTouchEnabled(true)
    isDragEnabled = true
    setScaleEnabled(true)
    setPinchZoom(true)
    setDrawGridBackground(false)
    legend.textColor = Color.Black.toArgb()
}

fun LineChart.configureBloodPressureAxes() {
    xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        granularity = 86400000f // 1 day
        valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return dateFormat.format(Date(value.toLong()))
            }
        }
        textColor = Color.Black.toArgb()
        setDrawGridLines(false)
    }

    axisLeft.apply {
        axisMinimum = 50f
        axisMaximum = 200f
        textColor = Color.Black.toArgb()
        setDrawGridLines(true)
        gridColor = Color.LightGray.toArgb()
    }

    axisRight.isEnabled = false
}

fun LineDataSet.styleLineDataSet(color: Int, fillColor: Int) {
    this.color = color
    lineWidth = 2.5f
    setDrawCircles(true)
    setCircleColor(color)
    circleRadius = 4f
    setDrawValues(false)
    mode = LineDataSet.Mode.CUBIC_BEZIER
    setDrawFilled(true)
    setFillColor(fillColor)
    fillAlpha = 100
    valueTextSize = 10f
    valueTypeface = Typeface.DEFAULT_BOLD
}

