package org.lifetrack.ltapp.ui.components.homescreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.ui.theme.Purple40

@Composable
fun ActivityRing(
    progress: Float, // 0.0f to 1.0f
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(70.dp)) {
            // Background Track
            drawArc(
                color = color.copy(alpha = 0.15f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            )
            // Progress Arc
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
    }
}

//@Composable
//fun GoalModule(
//    label: String,
//    value: String,
//    progress: Float,
//    color: Color,
//    icon: ImageVector,
//    modifier: Modifier = Modifier
//) {
//    val themeColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
//
//    Column(
//        modifier = modifier
//            .clip(RoundedCornerShape(16.dp))
//            .background(if (isSystemInDarkTheme()) Color.White.copy(0.05f) else Color.Black.copy(0.03f))
//            .padding(vertical = 12.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        ActivityRing(
//            progress = progress,
//            color = color,
//            icon = icon
//        )
//        Spacer(Modifier.height(10.dp))
//        Text(
//            text = label,
//            fontSize = 12.sp,
//            fontWeight = FontWeight.Bold,
//            color = themeColor
//        )
//        Text(
//            text = value,
//            fontSize = 10.sp,
//            color = if (isSystemInDarkTheme()) Color.Gray else Color.DarkGray
//        )
//    }
//}