package org.lifetrack.ltapp.view.components.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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

@Composable
fun FeatureGrid(navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            GlassActionCard(
                "Medical Timeline",
                Icons.Filled.BarChart
            ) { navController.navigate("med_timeline") }
        }
        item {
            GlassActionCard(
                "Appointments",
                Icons.Filled.CalendarMonth
            ) { navController.navigate("") }
        }
        item {
            GlassActionCard(
                "Follow Ups & Visits",
                Icons.AutoMirrored.Filled.MultilineChart
            ) { navController.navigate("") }
        }
        item {
            GlassActionCard(
                "Emergency Alerts",
                Icons.Filled.Notifications
            ) { navController.navigate("") }
        }
        item {
            GlassActionCard("Messaging & Referrals",
                Icons.AutoMirrored.Filled.Message) {
                navController.navigate("")
            }
        }
        item {
            GlassActionCard(
                "E-Prescriptions",
                Icons.Filled.LocalHospital
            ) { navController.navigate("") }
        }
        item {
            GlassActionCard("Reports & Analytics",
                Icons.Filled.DataExploration) {
                navController.navigate("") }
        }
        item {
            GlassActionCard(
                "Health Campaigns",
                Icons.Filled.Campaign) {
                navController.navigate("") }
        }
        item {
            GlassActionCard(
                "Help & Support",
                Icons.AutoMirrored.Filled.HelpCenter){
                navController.navigate("") }
        }
    }
}
