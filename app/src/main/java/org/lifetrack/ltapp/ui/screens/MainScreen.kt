package org.lifetrack.ltapp.ui.screens

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.lifetrack.ltapp.utility.ZetuZetuUtil.times
import org.lifetrack.ltapp.utility.transform
import org.lifetrack.ltapp.ui.theme.DEFAULT_PADDING
import kotlin.math.PI
import kotlin.math.sin


@RequiresApi(Build.VERSION_CODES.S)
private fun getRenderEffect(): RenderEffect {
    val blur = RenderEffect.createBlurEffect(
        80f, 80f, Shader.TileMode.MIRROR
    )

    val alphaMatrix = RenderEffect.createColorFilterEffect(
        ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, 50f, -5000f
                )
            )
        )
    )

    return RenderEffect.createChainEffect(alphaMatrix, blur)
}


@Composable
fun MainScreen() {
    val isMenuExtended = remember { mutableStateOf(false) }

    val fabAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing)
    )

    val clickAnimationProgress by animateFloatAsState(
        targetValue = if (isMenuExtended.value) 1f else 0f,
        animationSpec = tween(300)
    )

    val renderEffect =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            getRenderEffect().asComposeRenderEffect()
        else null

    MainScreen(
        renderEffect = renderEffect,
        fabAnimationProgress = fabAnimationProgress,
        clickAnimationProgress = clickAnimationProgress
    ) {
        isMenuExtended.value = !isMenuExtended.value
    }
}

@Composable
fun MainScreen(
    renderEffect: androidx.compose.ui.graphics.RenderEffect?,
    fabAnimationProgress: Float,
    clickAnimationProgress: Float,
    toggleAnimation: () -> Unit
) {
    Box(
        Modifier.fillMaxSize().padding(bottom = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        CustomBottomNavigation()

        Circle(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            animationProgress = fabAnimationProgress
        )

        FabGroup(
            renderEffect = renderEffect,
            animationProgress = fabAnimationProgress
        )

        FabGroup(
            renderEffect = null,
            animationProgress = fabAnimationProgress,
            toggleAnimation = toggleAnimation
        )

        Circle(
            color = Color.White,
            animationProgress = clickAnimationProgress
        )
    }
}

@Composable
fun Circle(color: Color, animationProgress: Float) {
    val value = sin(PI * animationProgress).toFloat()

    Box(
        Modifier
            .padding(DEFAULT_PADDING.dp)
            .size(56.dp)
            .scale(2 - value)
            .border(
                width = 2.dp,
                color = color.copy(alpha = color.alpha * value),
                shape = CircleShape
            )
    )
}

@Composable
fun CustomBottomNavigation() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(80.dp)
            .paint(
                painter = painterResource(org.lifetrack.ltapp.R.drawable.bottom_navigation),
                contentScale = ContentScale.FillHeight
            )
            .padding(horizontal = 40.dp)
    ) {
        listOf(Icons.Filled.CalendarToday, Icons.Filled.Group).forEach {
            IconButton(onClick = {}) {
                Icon(it, null, tint = Color.White)
            }
        }
    }
}

@Composable
fun FabGroup(
    animationProgress: Float,
    renderEffect: androidx.compose.ui.graphics.RenderEffect? = null,
    toggleAnimation: () -> Unit = {}
) {
    val expanded = animationProgress > 0.05f

    Box(
        Modifier
            .fillMaxSize()
            .graphicsLayer {
                this.renderEffect = renderEffect
                alpha = if (renderEffect != null) 0.99f else 1f
            }
            .padding(bottom = DEFAULT_PADDING.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        AnimatedFab(
            icon = Icons.Default.PhotoCamera,
            modifier = Modifier.padding(
                PaddingValues(bottom = 72.dp, end = 200.dp)
                    .times(FastOutSlowInEasing.transform(0f, 0.8f, animationProgress))
            ),
            opacity = animationProgress,
            clickable = expanded
        )

        AnimatedFab(
            icon = Icons.Default.Settings,
            modifier = Modifier.padding(
                PaddingValues(bottom = 110.dp)
                    .times(FastOutSlowInEasing.transform(0.1f, 0.9f, animationProgress))
            ),
            opacity = animationProgress,
            clickable = expanded
        )

        AnimatedFab(
            icon = Icons.Default.ShoppingCart,
            modifier = Modifier.padding(
                PaddingValues(bottom = 72.dp, start = 200.dp)
                    .times(FastOutSlowInEasing.transform(0.2f, 1f, animationProgress))
            ),
            opacity = animationProgress,
            clickable = expanded
        )

        AnimatedFab(
            modifier = Modifier.scale(1f - animationProgress),
            clickable = false
        )

        AnimatedFab(
            icon = Icons.Default.Add,
            modifier = Modifier
                .rotate(45f * animationProgress)
                .scale(1f + 0.15f * animationProgress),
            clickable = true,
            onClick = toggleAnimation,
            backgroundColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun AnimatedFab(
    modifier: Modifier,
    icon: ImageVector? = null,
    opacity: Float = 1f,
    clickable: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    onClick: () -> Unit = {}
) {
    if (opacity <= 0f) return

    FloatingActionButton(
        onClick = { if (clickable) onClick() },
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp
        ),
        containerColor = backgroundColor.copy(alpha = opacity),
        modifier = modifier
            .scale(1.25f)
            .graphicsLayer { alpha = opacity }
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = Color.White.copy(alpha = if (clickable) 1f else 0.5f)
            )
        }
    }
}
