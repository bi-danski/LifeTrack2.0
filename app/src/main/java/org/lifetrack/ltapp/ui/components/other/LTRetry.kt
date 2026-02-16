package org.lifetrack.ltapp.ui.components.other

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LTRetry(
    modifier: Modifier = Modifier,
    title: String = "Connection Lost",
    onDubClick: () -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            delay(5000)
            isExpanded = false
        }
    }

    Surface(
        modifier = modifier
            .widthIn(min = 44.dp, max = 240.dp)
            .animateContentSize(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.95f),
        tonalElevation = 4.dp,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .combinedClickable(
                    onClick = { isExpanded = true },
                    onDoubleClick = {
                        Log.d("Double-Click", "True")
                        onDubClick()
                    }
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.WifiOff,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = if (!isSystemInDarkTheme()) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onErrorContainer
            )

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandHorizontally(expandFrom = Alignment.Start),
                exit = shrinkHorizontally(shrinkTowards = Alignment.Start)
            ) {
                Row {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        color = if (!isSystemInDarkTheme()) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}