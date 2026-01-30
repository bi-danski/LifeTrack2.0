package org.lifetrack.ltapp.ui.components.homescreen.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.lifetrack.ltapp.ui.components.homescreen.rings.VitalMetricRing

@Composable
fun VitalsCard(
    heartRate: String = "78 bpm",
    bloodPressure: String = "120/80",
    spo2: String = "98%",
    temperature: String = "98.6°F"
) {
    GlassCard(
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 20.dp, bottom = 10.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VitalMetricRing(Modifier.weight(1f), "Heart Rate", heartRate, Icons.Default.Favorite, Color(0xFFE74C3C))
                VitalMetricRing(Modifier.weight(1f), "Blood Pressure", bloodPressure, Icons.Default.MonitorHeart, Color(0xFF3498DB))
                VitalMetricRing(Modifier.weight(1f), "SpO2", spo2, Icons.Default.Opacity, Color(0xFF2ECC71))
                VitalMetricRing(Modifier.weight(1f), "Temp", temperature, Icons.Default.Thermostat, Color(0xFFFFA000))
            }
        }
    }
}

