package org.lifetrack.ltapp.ui.components.homescreen.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DailyActivities(
    steps: Int = 6400,
    stepGoal: Int = 10000,
    waterLiters: Float = 1.5f,
    waterGoal: Float = 2.5f,
    sleepHours: Float = 6.5f,
    sleepGoal: Float = 8f,
    isAnimationEnabled: Boolean
) {
    GlassCard(
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                ActivityCard(
                    label = "Steps",
                    targetVal = "$stepGoal",
                    progress = (steps.toFloat() / stepGoal).coerceAtMost(1f),
                    color = Color(0xFF4CAF50),
                    icon = Icons.AutoMirrored.Filled.DirectionsRun,
                    modifier = Modifier.weight(1f),
                    animTime = 5000,
                    activityVal = "$steps",
                    isAnimEnabled = isAnimationEnabled
                )

                ActivityCard(
                    label = "Water",
                    targetVal = "${waterGoal}L",
                    progress = (waterLiters / waterGoal).coerceAtMost(1f),
                    color = Color(0xFF2196F3),
                    icon = Icons.Default.WaterDrop,
                    modifier = Modifier.weight(1f),
                    animTime = 7000,
                    activityVal = "${waterLiters}L",
                    isAnimEnabled = isAnimationEnabled
                )

                ActivityCard(
                    label = "Sleep",
                    targetVal = "${sleepGoal}h",
                    progress = (sleepHours / sleepGoal).coerceAtMost(1f),
                    color = Color(0xFF9C27B0),
                    icon = Icons.Default.Bedtime,
                    modifier = Modifier.weight(1f),
                    animTime = 10000,
                    activityVal = "${sleepHours}h",
                    isAnimEnabled = isAnimationEnabled
                )
            }
        }
    }
}