package org.lifetrack.ltapp.ui.components.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

//@Composable
//fun FeatureGrid(navController: NavController) {
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2),
//        modifier = Modifier.fillMaxHeight(),
//        verticalArrangement = Arrangement.spacedBy(15.dp),
//        horizontalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        item {
//            GlassActionCard(
//                "Medical Timeline",
//                Icons.Filled.BarChart
//            ) { navController.navigate("timeline") }
//        }
//        item {
//            GlassActionCard(
//                "Appointments",
//                Icons.Filled.CalendarMonth
//            ) { navController.navigate("appointments") }
//        }
//        item {
//            GlassActionCard(
//                "Follow Ups & Visits",
//                Icons.AutoMirrored.Filled.MultilineChart
//            ) { navController.navigate("FUV") }
//        }
//        item {
//            GlassActionCard(
//                "Emergency Alerts",
//                Icons.Filled.Notifications
//            ) { navController.navigate("alerts") }
//        }
//        item {
//            GlassActionCard(
//                "Messaging & Referrals",
//                Icons.AutoMirrored.Filled.Message
//            ) {
//                navController.navigate("ltChats")
//            }
//        }
//        item {
//            GlassActionCard(
//                "E-Prescriptions",
//                Icons.Filled.LocalHospital
//            ) { navController.navigate("prescriptions") }
//        }
//        item {
//            GlassActionCard(
//                "Reports & Analytics",
//                Icons.Filled.DataExploration
//            ) {
//                navController.navigate("analytics")
//            }
//        }
//        item {
//            GlassActionCard(
//                "Health Campaigns",
//                Icons.Filled.Campaign
//            ) {
//                navController.navigate("")
//            }
//        }
//        item {
//            GlassActionCard(
//                "Help & Support",
//                Icons.AutoMirrored.Filled.HelpCenter
//            ) {
//                navController.navigate("support")
//            }
//        }
//    }
//}

fun LazyGridScope.featureGridContent(navController: NavController) {
    val features = listOf(
        "Medical Timeline" to Icons.Filled.BarChart to "timeline",
        "Appointments" to Icons.Filled.CalendarMonth to "appointments",
        "Follow Ups & Visits" to Icons.AutoMirrored.Filled.MultilineChart to "FUV",
        "Emergency Alerts" to Icons.Filled.Notifications to "alerts",
        "Messaging & Referrals" to Icons.AutoMirrored.Filled.Message to "ltChats",
        "E-Prescriptions" to Icons.Filled.LocalHospital to "prescriptions",
        "Reports & Analytics" to Icons.Filled.DataExploration to "analytics",
        "Health Campaigns" to Icons.Filled.Campaign to "",
        "Help & Support" to Icons.AutoMirrored.Filled.HelpCenter to "support"
    )

    items(features) { (data, route) ->
        val (title, icon) = data
        GlassActionCard(title, icon) {
            if (route.isNotEmpty()) navController.navigate(route)
        }
    }
}