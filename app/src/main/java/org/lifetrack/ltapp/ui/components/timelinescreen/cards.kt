package org.lifetrack.ltapp.ui.components.timelinescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.lifetrack.ltapp.model.data.dclass.MedicalVisit
import org.lifetrack.ltapp.ui.theme.Purple40
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

@Composable
fun MedicalVisitCard(visit: MedicalVisit, isExpanded: Boolean,isSelected: Boolean,
    onToggleExpanded: () -> Unit,
    onToggleSelection: () -> Unit,
    onBookmark: () -> Unit,
    onShare: () -> Unit
) {
    val highlightColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.onSurface
    } else {
        Purple40
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { onToggleSelection() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = visit.diagnosis,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) Color(0xFFE1BEE7) else Color(0xFF2E3192)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = dateFormatter.format(visit.date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSystemInDarkTheme()) Color(0xFFE1BEE7) else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                StatusChip(status = visit.status)

                RecordActionsMenu(
                    onShare = onShare,
                    onDownload = { },
                    onBookmark = onBookmark
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                color = highlightColor,
                text = visit.doctor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium

            )

            Spacer(Modifier.height(8.dp))

            Text(
                color = highlightColor,
                text = visit.hospital,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                color = highlightColor,
                text = visit.treatment,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(4.dp))

            TextButton(
                onClick = onToggleExpanded,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(if (isExpanded) "Hide details" else "View details")
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

//                    if (visit.notes.isNotBlank()) {
//                        Text(
//                            text = "Practitioner Notes:",
//                            style = MaterialTheme.typography.labelLarge,
//                            color = MaterialTheme.colorScheme.primary,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                        Spacer(Modifier.height(2.dp))
//                        Text(
//                            text = visit.notes,
//                            style = MaterialTheme.typography.bodyMedium,
//                            modifier = Modifier.padding(bottom = 12.dp),
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    }
                    PractitionerNotes(note = visit.notes)
                    Spacer(Modifier.height(2.dp))

                    if (visit.attachments.isNotEmpty()) {
                        AttachmentsSection(attachments = visit.attachments)
                    }


                }
            }
        }
    }
}