package org.lifetrack.ltapp.ui.components.homescreen.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactEmergency
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.utility.customFormat
import org.lifetrack.ltapp.model.data.dclass.Appointment
import org.lifetrack.ltapp.ui.theme.Purple40


@Composable
fun ScheduleCard(appointmentCount: Int, nextAppointment: Appointment?,
                 onEmergencyClick: () -> Unit,
                 onEmergencyContactClick: () -> Unit
) {
    val themeColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
    val subTextColor = if (isSystemInDarkTheme()) Color.Gray else Color(0xFF5F6368)

    GlassCard(
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background
                ),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {

            Box(
                modifier = Modifier
                    .padding(1.dp)
                    .fillMaxWidth()
                    .weight(1.1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background( Purple40.copy(0.08f))
                    .border(1.dp,
                        if (isSystemInDarkTheme()) nextAppointment?.status?.color?.copy(0.3f) ?: MaterialTheme.colorScheme.secondary.copy(0.3f)
                        else nextAppointment?.status?.color?.copy(0.15f) ?: Purple40.copy(0.15f),
                        RoundedCornerShape(16.dp)
                    )
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
                                .fillMaxSize()
                                .padding(horizontal = 12.dp, vertical = 0.dp),
                            verticalAlignment = Alignment.CenterVertically,

                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(top = 1.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.EventAvailable,
                                    contentDescription = null,
                                    tint = themeColor,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    "NEXT",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = themeColor
                                )
                                Text(
                                    " UP",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = themeColor,
//                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.weight(1.6f)

                            ) {
                                Text(
                                    text = nextAppointment.doctor,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.5.sp,
                                    color = themeColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = nextAppointment.hospital,
                                    fontSize = 16.5.sp ,
                                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.secondary else
                                        MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Black,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = nextAppointment.scheduledAt.customFormat("dd MMM yyyy HH:mm a"),
//                                        "EEE, MMM dd, yyyy '@' hh:mm a"),
                                    fontSize = 15.5.sp,
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
                    .weight(0.9f)
                    .background(
                        Color.Transparent
                    ),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    onClick = onEmergencyClick,
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight(),
//                        .graphicsLayer(scaleX = pulseScale, scaleY = pulseScale),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSystemInDarkTheme()) Color(0xFFE74C3C).copy(0.5f)
                        else Color(0xFFE74C3C).copy(0.8f)
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.WifiTethering,
                            contentDescription = "Trigger Emergency Call",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "SOS",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .weight(1.2f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Purple40.copy(alpha = 0.08f))
//                        .border(1.dp,
//                            if (isSystemInDarkTheme()) MaterialTheme.colorScheme.secondary else Purple40.copy(alpha = 0.15f),
//                            RoundedCornerShape(16.dp)
//                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onEmergencyContactClick() }
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        if (isSystemInDarkTheme()) Color.Green.copy(alpha = 0.15f) else Purple40.copy(alpha = 0.15f),
                                        RoundedCornerShape(18.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContactEmergency,
                                    contentDescription = "Emergency Contact",
                                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.secondary else Purple40,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = "E-CONTACT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = themeColor,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(subTextColor.copy(alpha = 0.3f))
                        )

                        Column(
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = appointmentCount.toString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = themeColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Appointments",
                                fontSize = 12.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.Black,
                                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.secondary else Purple40,
//                                    Color(0xFF5F6368),
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}