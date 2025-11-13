package org.lifetrack.ltapp.view.components.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.lifetrack.ltapp.view.ui.theme.Purple40
import org.lifetrack.ltapp.view.ui.theme.Purple80

@Composable
fun GlassFloatingActionButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    val shape = CircleShape
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color.Transparent,
        contentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
        modifier = Modifier
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Purple80.copy(0.5f),
                        Purple80.copy(alpha = 0.5f)
                    )
                ),
                shape = shape
            )
    ) {
        content()
    }
}
