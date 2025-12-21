package org.lifetrack.ltapp.model.data.dclass

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItemData(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val rightIcon: ImageVector?,
)

data class ToggleItemData(
    val title: String,
    var icon: ImageVector,
)

val menuListItems: Collection<MenuItemData> = mutableListOf(
    MenuItemData("Emergency Contacts", Icons.Filled.Emergency, "epidemic_alert", rightIcon = null),
    MenuItemData("About LifeTrack", Icons.Filled.Info, "about", rightIcon = null)
)





