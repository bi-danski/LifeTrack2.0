package org.lifetrack.ltapp.ui.components.homescreen.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.ui.components.homescreen.HealthMetric
import org.lifetrack.ltapp.ui.theme.Purple40

@Composable
fun HealthSummaryCard (bloodPressure: String ="120/80", heartRate: String = "78 bpm", temperature: String ="98.6 F") {
    GlassCard(
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Transparent)
    ){
        Column(Modifier.padding(16.dp)) {
            Text(
                "Health Summary",
                fontWeight = FontWeight.Black,
                fontSize = 21.sp,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
            )

            Spacer(Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                HealthMetric(
                    "BP",
                    bloodPressure,
                    icon = Icons.Default.MonitorHeart,
                    iconColor = Color(0xFFE74C3C)
                )
                Spacer(Modifier.width(10.dp))
                HealthMetric(
                    "BPM",
                    heartRate,
                    Icons.Default.Favorite,
                    iconColor = Color(0xFFEC407A)
                )
                Spacer(Modifier.width(10.dp))
                HealthMetric(
                    "Temp",
                    temperature,
                    Icons.Default.Thermostat,
                    iconColor = Color(0xFFFFA000)
                )
            }
        }
    }
}