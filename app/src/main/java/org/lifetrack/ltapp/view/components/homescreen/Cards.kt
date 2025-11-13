package org.lifetrack.ltapp.view.components.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.view.ui.theme.Purple40
import org.lifetrack.ltapp.view.ui.theme.Purple80

@Composable
fun GlassCard(shape: Shape, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
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
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
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
            .background(
                color = MaterialTheme.colorScheme.background.copy(0.08f),
//                brush = Brush.verticalGradient(
//                    listOf(
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
//                    )
//                ),
//                shape = shape
            ),
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
fun TodayScheduleCard() {
    GlassCard(shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
//            .background(color = MaterialTheme.colorScheme.surfaceVariant)
//            .pulsate()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Today's Schedule",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier
                ) {
                    Text(
                        textAlign = TextAlign.End,
                        text = "7",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                    )
                }
                Spacer(Modifier.height(4.dp))

                Text(text = "Appointments",
//                    fontSize = 64.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40)

            }


            Column(modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(modifier = Modifier.padding(bottom = 10.dp)){
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications,
                            contentDescription = "Upcoming",
                            modifier = Modifier
                                .size(45.dp)
                        )
                    }
                    Text(
                        text = "14",
                        textAlign = TextAlign.Right,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Upcoming",
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                )
            }
        }
    }
}

