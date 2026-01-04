package org.lifetrack.ltapp.ui.components.fuvscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.lifetrack.ltapp.core.utility.customFormat
import org.lifetrack.ltapp.model.data.dclass.HospitalVisit
import org.lifetrack.ltapp.model.data.dclass.UpcomingVisit
import org.lifetrack.ltapp.ui.theme.Purple40


@Composable
fun FollowUpDetailCard(visit: UpcomingVisit) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(0.dp),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = if (isSystemInDarkTheme()) Color(0xFF1E1E1E)
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
//            MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.EventNote,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = visit.location,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
//                        fontSize = 14.sp
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = visit.timestamp.customFormat("hh:mm a"),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        softWrap = false
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Column(modifier = Modifier.padding(start = 28.dp)) {
                Text(
                    text = visit.treatment,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                )
                Text(
                    text = visit.timestamp.customFormat("EEE dd MMM yyyy"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun HospitalVisitNode(visit: HospitalVisit) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalHospital,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = visit.hospitalName,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = visit.department,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        visit.subVisits.forEachIndexed { index, sub ->
            val isLast = index == visit.subVisits.lastIndex
            Row(modifier = Modifier.height(IntrinsicSize.Min).padding(start = 36.dp)) {
                Box(modifier = Modifier.width(24.dp).fillMaxHeight()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 4f
                        val lineColor = Color.LightGray

                        drawLine(
                            color = lineColor,
                            start = Offset(0f, 0f),
                            end = Offset(0f, if (isLast) size.height / 2 else size.height),
                            strokeWidth = strokeWidth
                        )
                        drawLine(
                            color = lineColor,
                            start = Offset(0f, size.height / 2),
                            end = Offset(15.dp.toPx(), size.height / 2),
                            strokeWidth = strokeWidth
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sub.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = sub.timestamp.customFormat("MMM dd/y"),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}