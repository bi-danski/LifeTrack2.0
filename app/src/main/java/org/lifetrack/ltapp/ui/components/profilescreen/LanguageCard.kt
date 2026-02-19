package org.lifetrack.ltapp.ui.components.profilescreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.model.data.dclass.LanguageOption
import org.lifetrack.ltapp.ui.theme.Purple40
import org.lifetrack.ltapp.ui.theme.Purple80

@Composable
fun LanguageCard(language: LanguageOption, isSelected: Boolean, onClick: () -> Unit) {
    val colorScheme: androidx.compose.material3.ColorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) { if (isSystemInDarkTheme()) Purple80.copy(alpha = 0.3f) else Purple40.copy(alpha = 0.15f) }
            else { if (isSystemInDarkTheme()) Color(0xFF1E1E1E) else colorScheme.surfaceVariant.copy(alpha = 0.5f) }
        ),
        border = if (isSelected) BorderStroke(2.dp, if (isSystemInDarkTheme()) Purple80 else Purple40) else null
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = language.flag, fontSize = 32.sp, modifier = Modifier.padding(end = 16.dp) )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = language.nativeName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground
                )
                Text(
                    text = language.name,
                    fontSize = 13.sp,
                    color = colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = if (isSystemInDarkTheme()) Purple80 else Purple40,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}