package org.lifetrack.ltapp.ui.components.prescriptscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.model.data.dclass.Prescription
import org.lifetrack.ltapp.ui.components.homescreen.cards.GlassCard
import org.lifetrack.ltapp.ui.theme.HospitalBlue


@Composable
fun PrescriptionCard(
    prescription: Prescription,
    isExpired: Boolean,
    onRefillRequest: (Prescription) -> Unit = {},
    onCardClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val isRefillDue = prescription.status == "Refill Due" && !isExpired
    val headerColor = when {
        isExpired -> Color(0xFFD32F2F)
        isDark -> Color(0xFFE1BEE7)
        else -> Color(0xFF4A148C)
    }
    val bodyTextColor = if (isDark) Color.White else Color.DarkGray
    val dateTextColor = if (isExpired) Color(0xFFD32F2F) else Color.Gray
    val dateWeight = if (!isExpired) FontWeight.Bold else FontWeight.ExtraBold

    GlassCard(
        modifier = Modifier
            .clickable(onClick = onCardClick)
            .fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CardDefaults.cardColors(
            if (isSystemInDarkTheme()) Color(0xFF1E1E1E)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = prescription.medicationName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = headerColor
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = prescription.dosage,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color(0xFFCE93D8) else bodyTextColor //Pink 40
                    )
                }
                if (isExpired) {
                    StatusBadge(text = "EXPIRED", color = Color(0xFFD32F2F))
                } else if (isRefillDue) {
                    StatusBadge(text = "REFILL DUE", color = Color(0xFFEF6C00))
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = prescription.instructions,
                style = MaterialTheme.typography.bodyMedium,
                color = bodyTextColor,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold
            )

            if (!isExpired) {
                Spacer(Modifier.height(16.dp))
                if (prescription.refillProgress > 0) {
                    LinearProgressIndicator(
                        progress = { prescription.refillProgress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = if (isRefillDue) Color(0xFFEF6C00) else Color(0xFF2E7D32),
                        trackColor = if (isDark) Color.White.copy(0.1f) else Color.Black.copy(0.05f),
                    )
                }

                if (isRefillDue) {
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { onRefillRequest(prescription) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) Color(0xFFFFB74D).copy(0.15f) else Color(0xFFFFF3E0),
                            contentColor = if (isDark) Color(0xFFFFB74D) else Color(0xFFE65100)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFEF6C00).copy(0.3f))
                    ) {
                        Icon(Icons.Default.Share, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Request Doctor Refill", fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Ends: ${prescription.endDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color =  if (isSystemInDarkTheme()) dateTextColor else (if (isExpired) Color(0xFFD32F2F) else HospitalBlue),
                    fontWeight = dateWeight
                )
                Text(
                    text = "Dr. ${prescription.prescribedBy}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color(0xFFCE93D8) else Color(0xFF4A148C)
                )
            }
        }
    }
}
