package org.lifetrack.ltapp.ui.components.homescreen.rings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VitalMetricRing(modifier: Modifier = Modifier, label: String, value: String, icon: ImageVector, accentColor: Color ) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(75.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.05f),
            border = BorderStroke(
                width = 1.6.dp,
                brush = Brush.linearGradient(
                    listOf(accentColor.copy(alpha = 0.65f), Color.Transparent)
                )
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = value,
                    maxLines = 1,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    color = accentColor.copy(0.7f)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            maxLines = 2,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            overflow = TextOverflow.Visible,
            color = accentColor
        )
    }
}