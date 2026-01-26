package org.lifetrack.ltapp.model.data.dclass

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.Color
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

data class StatusChipData(
    val label: String,
    val color: Color,
    val icon: ImageVector
)

val menuListItems: Collection<MenuItemData> = mutableListOf(
    MenuItemData("Emergency Contacts", Icons.Filled.Emergency, "epidemic_alert", rightIcon = null),
    MenuItemData("About LifeTrack", Icons.Filled.Info, "about", rightIcon = null)
)

data class LtSettings(
    val notifications: Boolean = true,
    val emailNotifications: Boolean = false,
    val smsNotifications: Boolean = false,
    val animations: Boolean = true,
    val dataConsent: Boolean = false,
    val reminders: Boolean = true
)




