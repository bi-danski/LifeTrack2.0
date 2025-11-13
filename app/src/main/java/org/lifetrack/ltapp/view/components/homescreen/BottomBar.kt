package org.lifetrack.ltapp.view.components.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.lifetrack.ltapp.view.ui.theme.Purple40

@Composable
fun AppBottomBar() {
    var selected by remember { mutableIntStateOf(0) }
    val shape = RoundedCornerShape(24.dp)
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Purple40.copy(0.4f),
                        Purple40.copy(0.4f),
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        Purple40.copy(0.4f)
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    )
                ),
                shape = shape
            ),
        containerColor = Color.Transparent
    ) {
        NavigationBarItem(
            selected = selected == 0,
            onClick = { selected = 0 },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selected == 2,
            onClick = { selected = 2 },
            icon = { Icon(Icons.Filled.BarChart, contentDescription = "Reports") },
            label = { Text("Medical Records", textAlign = TextAlign.Justify) }
        )
        NavigationBarItem(
            selected = selected == 1,
            onClick = { selected = 1},
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}
