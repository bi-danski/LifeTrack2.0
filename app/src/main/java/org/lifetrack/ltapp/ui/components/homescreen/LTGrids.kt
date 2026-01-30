package org.lifetrack.ltapp.ui.components.homescreen

import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpCenter
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.MultilineChart
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.DataExploration
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Notifications
import org.lifetrack.ltapp.ui.components.homescreen.cards.GlassActionCard
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch


fun LazyGridScope.featureGridContent() {
    val features = listOf(
        "Medical Timeline" to Icons.Filled.BarChart to "timeline",
        "Appointments" to Icons.Filled.CalendarMonth to "appointments",
        "Follow Ups & Visits" to Icons.AutoMirrored.Filled.MultilineChart to "FUV",
        "Emergency Alerts" to Icons.Filled.Notifications to "alerts",
        "Messaging & Referrals" to Icons.AutoMirrored.Filled.Message to "ltChats",
        "E-Prescriptions" to Icons.Filled.LocalHospital to "prescriptions",
        "Reports & Analytics" to Icons.Filled.DataExploration to "analytics",
        "Health Campaigns" to Icons.Filled.Campaign to "bar-bra",
        "Help & Support" to Icons.AutoMirrored.Filled.HelpCenter to "support"
    )

    items(features) { (data, route) ->
        val (title, icon) = data
        GlassActionCard(title, icon) {
            if (route.isNotEmpty()) LTNavDispatch.navigate(route)
        }
    }
}