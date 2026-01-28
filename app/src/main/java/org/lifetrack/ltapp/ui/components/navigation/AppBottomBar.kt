package org.lifetrack.ltapp.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.ui.navigation.LTNavDispatcher
import org.lifetrack.ltapp.ui.theme.Purple40


@Composable
fun AppBottomBar() {
    val currentRoute by LTNavDispatcher.currentRoute
    val isDark = isSystemInDarkTheme()
    val shape = RoundedCornerShape(28.dp)
    val activeColor = if (isDark) Color(0xFF03A9F4) else Purple40
    val inactiveColor = activeColor.copy(0.4f)

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 1.dp)
            .clip(shape)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White.copy(0.2f), Color.White.copy(0.05f))
                ),
                shape = shape
            )
            .background(
                color = if (isDark) Color.White.copy(0.08f) else Color.Black.copy(0.04f),
                shape = shape
            ),
        containerColor = Color.Transparent,
        tonalElevation = 0.dp
    ) {
        val items = listOf(
            NavigationTab("Home", "home", Icons.Filled.Home),
            NavigationTab("Medical Records", "analytics", Icons.Filled.BarChart),
            NavigationTab("Profile", "profile", Icons.Filled.AccountCircle)
        )

        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) LTNavDispatcher.navigate(item.route)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) activeColor else inactiveColor,
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
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (isSelected) activeColor else inactiveColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

//@Composable
//fun LTAppBottomBar() {
//    var selected by remember { mutableIntStateOf(0) }
//    val shape = RoundedCornerShape(28.dp)
//    val isDark = isSystemInDarkTheme()
//
//    NavigationBar(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 12.dp)
//            .clip(shape)
//            .border(
//                width = 1.dp,
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        Color.White.copy(alpha = 0.2f),
//                        Color.White.copy(alpha = 0.05f)
//                    )
//                ),
//                shape = shape
//            )
//            .background(
//                color = if (isDark) Color.White.copy(0.05f) else Color.Black.copy(0.03f),
//                shape = shape
//            ),
//        containerColor = Color.Transparent,
//        tonalElevation = 0.dp,
//        windowInsets = NavigationBarDefaults.windowInsets
//    ) {
//        val navItems = listOf(
//            Triple(0, Icons.Filled.Home, "Home"),
//            Triple(2, Icons.Filled.BarChart, "Medical Records"),
//            Triple(1, Icons.Filled.AccountCircle, "Profile")
//        )
//
//        navItems.forEach { (index, icon, label) ->
//            val isSelected = selected == index
//            val activeColor = if (isDark) Color(0xFF03A9F4) else Purple40
//
//            NavigationBarItem(
//                selected = isSelected,
//                onClick = {
//                    selected = index
//                    if (index == 1) LTNavDispatcher.navigate("profile")
//                },
//                icon = {
//                    Icon(
//                        imageVector = icon,
//                        contentDescription = label,
//                        tint = if (isSelected) activeColor else activeColor.copy(alpha = 0.4f),
//                        modifier = Modifier.size(26.dp)
//                    )
//                },
//                label = {
//                    Text(
//                        text = label,
//                        textAlign = TextAlign.Center,
//                        style = MaterialTheme.typography.labelSmall.copy(
//                            fontSize = 11.sp,
//                            lineHeight = 12.sp,
//                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
//                        ),
//                        color = if (isSelected) activeColor else activeColor.copy(alpha = 0.6f),
//                        maxLines = 2,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                },
//                colors = NavigationBarItemDefaults.colors(
//                    indicatorColor = Color.Transparent
//                )
//            )
//        }
//    }
//}