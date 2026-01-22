package org.lifetrack.ltapp.ui.components.appointscreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.lifetrack.ltapp.core.utility.customFormat
import org.lifetrack.ltapp.model.data.dclass.Appointment
import org.lifetrack.ltapp.ui.theme.Purple40

@Composable
fun AppointmentCard(appointment: Appointment) {
    val isDark = isSystemInDarkTheme()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E1E)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = appointment.doctor,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isDark) Color(0xFFE1BEE7) else Color(0xFF2E3192)
                )

                Text(
                    text = "${appointment.dateTime.customFormat("MMM dd, yyyy")} - ${appointment.dateTime.customFormat("hh:mm a")}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDark) Color(0xFFB39DDB) else Purple40
                )

                Text(
                    text = appointment.hospital,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = if (isDark) Color(0xFFB39DDB) else Purple40
                )
            }
            Icon(
                imageVector = Icons.Default.PendingActions,
                contentDescription = "Details",
                tint = if (isDark) Color(0xFF81C784) else Color(0xFF4CAF50),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}