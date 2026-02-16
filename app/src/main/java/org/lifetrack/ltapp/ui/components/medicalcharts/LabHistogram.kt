package org.lifetrack.ltapp.ui.components.medicalcharts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
