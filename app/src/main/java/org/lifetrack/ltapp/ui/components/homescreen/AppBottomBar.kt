package org.lifetrack.ltapp.ui.components.homescreen

import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.R
import org.lifetrack.ltapp.model.data.dclass.NavigationTab
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import org.lifetrack.ltapp.ui.theme.BlueFulani
import org.lifetrack.ltapp.ui.theme.Pink80
import org.lifetrack.ltapp.ui.theme.Purple40

@Composable
fun AppBottomBar() {
    val currentRoute by LTNavDispatch.currentRoute
    var selectedTab by remember { mutableStateOf(currentRoute) }

    LaunchedEffect(currentRoute) {
        selectedTab = currentRoute
    }
    val shape = RoundedCornerShape(28.dp)
    val inactiveColor = if (isSystemInDarkTheme()) BlueFulani else Purple40
    val activeColor = if (isSystemInDarkTheme()) Pink80 else BlueFulani

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 1.dp)
            .clip(shape)
            .background(color = Purple40.copy(0.08f), shape = shape),
        containerColor = Color.Transparent,
        tonalElevation = 0.dp
    ) {

        navigationTabs.forEach { item ->
            NavigationBarItem(
                selected = selectedTab == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        LTNavDispatch.navigate(item.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = if (selectedTab == item.route) activeColor else inactiveColor,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.labelRes),
                        maxLines = 2,
                        overflow = TextOverflow.Visible,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        ),
                        color = if (selectedTab == item.route) activeColor else inactiveColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

val navigationTabs = listOf(
    NavigationTab(R.string.home, "home", Icons.Filled.Home),
    NavigationTab(R.string.medical_records, "analytics", Icons.Filled.BarChart),
    NavigationTab(R.string.profile, "profile", Icons.Filled.AccountCircle)
)
