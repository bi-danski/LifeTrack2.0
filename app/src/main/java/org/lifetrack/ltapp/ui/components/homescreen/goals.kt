package org.lifetrack.ltapp.ui.components.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.ui.theme.Purple40


@Composable
fun GoalModule(
    label: String,
    value: String,
    progress: Float,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val themeColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40

    val animatedProgress = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = 1000,
                easing = androidx.compose.animation.core.FastOutSlowInEasing
            )
        )
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(themeColor.copy(0.08f))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActivityRing(
            progress = animatedProgress.value,
            color = color,
            icon = icon
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = themeColor
        )
        Text(
            text = value,
            fontSize = 10.sp,
            color = if (isSystemInDarkTheme()) Color.Gray else Color.DarkGray
        )
    }
}