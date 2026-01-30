package org.lifetrack.ltapp.ui.components.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.model.data.dclass.NavigationTab
import org.lifetrack.ltapp.ui.navigation.LTNavDispatcher
import org.lifetrack.ltapp.ui.theme.BlueFulani
import org.lifetrack.ltapp.ui.theme.Purple40


@Composable
fun AppBottomBar() {
    val currentRoute by LTNavDispatcher.currentRoute
    val shape = RoundedCornerShape(28.dp)
    val inactiveColor = Purple40

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 1.dp)
            .clip(shape)
//            .border(
//                width = 1.dp,
//                brush = Brush.verticalGradient(
//                    colors = listOf(Color.White.copy(0.2f), Color.White.copy(0.05f))
//                ),
//                shape = shape
//            )
            .background(color = Purple40.copy(0.08f), shape = shape),
        containerColor = Color.Transparent,
        tonalElevation = 0.dp
    ) {

        navigationTabs.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { if (!isSelected) LTNavDispatcher.navigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) BlueFulani else inactiveColor,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        maxLines = 2,
                        overflow = TextOverflow.Visible,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        ),
                        color = if (isSelected) BlueFulani else inactiveColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

val navigationTabs = mutableListOf(
    NavigationTab("Home", "home", Icons.Filled.Home),
    NavigationTab("Medical Records", "analytics", Icons.Filled.BarChart),
    NavigationTab("Profile", "profile", Icons.Filled.AccountCircle)
)