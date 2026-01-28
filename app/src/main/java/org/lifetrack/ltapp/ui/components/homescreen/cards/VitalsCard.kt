package org.lifetrack.ltapp.ui.components.homescreen.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.ui.theme.Purple40

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
                .padding(0.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
//            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VitalsCircleMetric(Modifier.weight(1f), "Heart Rate", heartRate, Icons.Default.Favorite, Color(0xFFE74C3C))
                VitalsCircleMetric(Modifier.weight(1f), "Blood Pressure", bloodPressure, Icons.Default.MonitorHeart, Color(0xFF3498DB))
                VitalsCircleMetric(Modifier.weight(1f), "SpO2", spo2, Icons.Default.Opacity, Color(0xFF2ECC71))
                VitalsCircleMetric(Modifier.weight(1f), "Temp", temperature, Icons.Default.Thermostat, Color(0xFFFFA000))
            }
        }
    }
}

@Composable
fun VitalsCircleMetric(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    accentColor: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(75.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.05f),
            border = BorderStroke(
                width = 1.6.dp,
                brush = Brush.linearGradient(
                    listOf(accentColor.copy(alpha = 0.65f), Color.Transparent)
                )
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = value,
                    maxLines = 1,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            maxLines = 1,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Black,
            overflow = TextOverflow.Ellipsis,
            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
//                .copy(alpha = 0.6f)
        )
    }
}