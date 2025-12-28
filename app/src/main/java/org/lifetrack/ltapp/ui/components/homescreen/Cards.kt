package org.lifetrack.ltapp.ui.components.homescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.model.data.dclass.Appointment
import org.lifetrack.ltapp.ui.theme.Purple40
import org.lifetrack.ltapp.ui.theme.Purple80


@Composable
fun GlassCard(
    shape: Shape,
    color: CardColors = CardDefaults.cardColors( containerColor = Color.Transparent ),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
//                        Purple40.copy(0.4f),
                        Purple80.copy(0.05f),
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    )
                ),
                shape = shape
            ),
        colors = color,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        content = { Column(content = content) }
    )
}

@Composable
fun GlassActionCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    val shape: Shape = RoundedCornerShape(22.dp)
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
//            .width(170.dp)
            .clip(shape)
            .background( color = MaterialTheme.colorScheme.background.copy(0.08f) ),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.primary.copy(0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(if(isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon,
                    contentDescription = title,
                    tint = if(isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else Purple80
                )
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = if(isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                maxLines = 2,
                fontWeight = FontWeight.Bold
//                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TodayScheduleCard(
    appointmentCount: Int,
    nextAppointment: Appointment?
) {
    val themeColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
    val subTextColor = if (isSystemInDarkTheme()) Color.Gray else Color(0xFF5F6368)

    GlassCard(
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "TODAY'S SCHEDULE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = themeColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = appointmentCount.toString(),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = themeColor,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Text(
                    text = "Appointments",
                    fontWeight = FontWeight.SemiBold,
                    color = themeColor
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier.weight(1.2f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "NEXT UP",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSystemInDarkTheme()) subTextColor else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (nextAppointment != null) {
                    Text(
                        text = nextAppointment.time,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = themeColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = nextAppointment.doctor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = themeColor
                    )
                    Text(
                        text = nextAppointment.hospital,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End,
                        color = if (isSystemInDarkTheme()) subTextColor else MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "All Caught Up!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HealthSummaryCard (
    bloodPressure: String ="120/80",
    heartRate: String = "78 bpm",
    temperature: String ="98.6 F"
) {
    GlassCard(
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        Column(Modifier.padding(16.dp)) {
            Text(
                "Health Summary",
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
            )

            Spacer(Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                HealthMetric(
                    "BP",
                    bloodPressure,
                    Icons.Default.MonitorHeart
                )
                Spacer(Modifier.width(10.dp))
                HealthMetric(
                    "BPM",
                    heartRate,
                    Icons.Default.Favorite
                )
                Spacer(Modifier.width(10.dp))
                HealthMetric(
                    "Temp",
                    temperature,
                    Icons.Default.Thermostat
                )
            }
        }
    }
}

