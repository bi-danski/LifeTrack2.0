package org.lifetrack.ltapp.model.data.dclass

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NewReleases
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

val statusChips = listOf(
    StatusChipData("Upcoming", Color(0xFF6200EE), Icons.Default.CalendarMonth),
    StatusChipData("Attended", Color(0xFF2ECC71), Icons.Default.CheckCircle),
    StatusChipData("Recently Booked", Color(0xFF7E57C2), Icons.Default.NewReleases),
    StatusChipData("Rescheduled", Color(0xFFE67E22), Icons.Default.History),
    StatusChipData("Dismissed", Color.Gray, Icons.Default.Cancel)
)





