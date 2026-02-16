package org.lifetrack.ltapp.ui.components.medicalcharts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun VitalsHeatMap(riskMatrix: List<List<Float>>) {
    val days = listOf("M", "T", "W", "T", "F", "S", "S")
    val times = listOf("6a", "10a", "2p", "6p", "10p", "2a")

    Column {
        Row(Modifier.fillMaxWidth().padding(start = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            times.forEach { Text(it, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), fontSize = 9.sp) }
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
