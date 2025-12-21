package org.lifetrack.ltapp.ui.components.fuvscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.unit.dp
import org.lifetrack.ltapp.model.data.dclass.HospitalVisit
import org.lifetrack.ltapp.ui.theme.Purple40


@Composable
fun FollowUpDetailCard(location: String, time: String, treatment: String, date:String) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.EventNote, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(Modifier.width(8.dp))
                Text(
                    "$location @ $time",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = treatment,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                modifier = Modifier.padding(start = 32.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = date,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 32.dp)
            )
        }
    }
}

@Composable
fun HospitalVisitNode(visit: HospitalVisit) {
    Column(
        modifier = Modifier.padding(bottom = 20.dp)
    ) {
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
                        Icons.Default.LocalHospital,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        visit.hospitalName,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        visit.department,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        visit.subVisits.forEach { subVisit ->
            Row(modifier = Modifier.height(IntrinsicSize.Min).padding(start = 36.dp)) {
                Box(modifier = Modifier.width(2.dp).fillMaxHeight()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 4f
                        )
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, size.height / 2),
                            end = Offset(20f, size.height / 2),
                            strokeWidth = 4f
                        )
                    }
                }
                Text(
                    text = subVisit,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
//                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}