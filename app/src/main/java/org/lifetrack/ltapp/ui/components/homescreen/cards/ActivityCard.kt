package org.lifetrack.ltapp.ui.components.homescreen.cards

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.lifetrack.ltapp.ui.components.homescreen.rings.ActivityRing


@Composable
fun ActivityCard(
    label: String,
    activityVal: String,
    targetVal: String,
    progress: Float,
    color: Color,
    icon: ImageVector,
    animTime: Int,
    modifier: Modifier = Modifier,
    isAnimEnabled: Boolean
) {
    val animatedProgress = remember { Animatable(0f) }

    if (isAnimEnabled){
        LaunchedEffect(progress) {
            animatedProgress.animateTo(
                targetValue = progress,
                animationSpec = tween(
                    durationMillis = animTime,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
            .background(color.copy(alpha = 0.05f))
            .padding(vertical = 4.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp,
            color = color.copy(alpha = 0.9f)
        )

        Spacer(Modifier.height(8.dp))

        Box(contentAlignment = Alignment.Center) {
            ActivityRing(
                progress = animatedProgress.value,
                color = color,
                icon = icon,
                modifier = Modifier.size(90.dp),
                activityVal = activityVal

            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = targetVal,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Black,
            color = color
        )
    }
}