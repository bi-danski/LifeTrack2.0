package org.lifetrack.ltapp.ui.components.medicalcharts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.lifetrack.ltapp.model.data.dclass.LabTest

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
            Box(Modifier.fillMaxWidth(0.4f).fillMaxHeight().align(Alignment.Center).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)))
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