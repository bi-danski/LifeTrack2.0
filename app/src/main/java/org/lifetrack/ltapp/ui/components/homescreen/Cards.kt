package org.lifetrack.ltapp.ui.components.homescreen

//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.RepeatMode
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.infiniteRepeatable
//import androidx.compose.animation.core.rememberInfiniteTransition
//import androidx.compose.animation.core.tween
//import androidx.compose.ui.text.style.TextAlign
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WifiTethering
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.core.utils.customFormat
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
            .background(color = MaterialTheme.colorScheme.background.copy(0.08f)),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.primary.copy(0.1f)
        ),
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
                    .background(if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else Purple80
                )
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
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
    nextAppointment: Appointment?,
    onEmergencyClick: () -> Unit = {}
) {
//    val infiniteTransition = rememberInfiniteTransition(label = "SOSPulse")
//    val pulseScale by infiniteTransition.animateFloat(
//        initialValue = 1f,
//        targetValue = 1.1f,
//        animationSpec = Repeatable(
//            animation = tween(1000, easing = FastOutSlowInEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "Pulse"
//    )
    val themeColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
    val subTextColor = if (isSystemInDarkTheme()) Color.Gray else Color(0xFF5F6368)
    val highlightColor = if (isSystemInDarkTheme()) Color.Green else MaterialTheme.colorScheme.secondary

    GlassCard(
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(themeColor.copy(alpha = 0.08f))
            ) {
                if (nextAppointment != null) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(6.dp)
                                .background(nextAppointment.status.color)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(0.7f)
                                    .padding(top = 5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EventAvailable,
                                    contentDescription = null,
                                    tint = themeColor,
                                    modifier = Modifier.size(26.dp)
                                )
                                Text(
                                    "NEXT",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = themeColor
                                )
                                Text(
                                    "UP",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = themeColor,
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.weight(1.6f)
                            ) {
                                Text(
                                    text = nextAppointment.doctor,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = themeColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = nextAppointment.hospital,
                                    fontSize = 13.5.sp,
                                    color = highlightColor,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = nextAppointment.dateTime.customFormat("hh:mm a"),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = themeColor
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No Appointments Today",
                        modifier = Modifier.align(Alignment.Center),
                        fontWeight = FontWeight.Bold,
                        color = subTextColor
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.9f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    onClick = onEmergencyClick,
                    modifier = Modifier
                        .weight(1.3f)
                        .fillMaxHeight(),
//                        .graphicsLayer(scaleX = pulseScale, scaleY = pulseScale),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE74C3C))
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.WifiTethering,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "EMERGENCY",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSystemInDarkTheme()) Color.White.copy(0.08f) else Color.Black.copy(0.04f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = appointmentCount.toString(),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = themeColor
                        )
                        Text(
                            text = "Appointments",
                            fontSize = 13.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            color = highlightColor,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HealthSummaryCard(
    bloodPressure: String = "120/80",
    heartRate: String = "78 bpm",
    temperature: String = "98.6 F"
) {
    val themeColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40

    GlassCard(
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
//            .wrapContentHeight()
    ) {
        Column(Modifier.padding(0.dp)) {
            Text(
                text = "Health Summary",
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
                style = MaterialTheme.typography.titleMedium,
                color = themeColor
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(themeColor.copy(alpha = 0.05f))
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    HealthMetric("BP", bloodPressure, Icons.Default.MonitorHeart)
                }

                Box(modifier = Modifier.width(1.dp).height(30.dp).background(themeColor.copy(0.1f)))

                Box(modifier = Modifier.weight(1f)) {
                    HealthMetric("BPM", heartRate, Icons.Default.Favorite)
                }

                Box(modifier = Modifier.width(1.dp).height(30.dp).background(themeColor.copy(0.1f)))

                Box(modifier = Modifier.weight(1f)) {
                    HealthMetric("Temp", temperature, Icons.Default.Thermostat)
                }
            }
        }
    }
}