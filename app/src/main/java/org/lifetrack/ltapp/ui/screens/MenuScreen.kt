package org.lifetrack.ltapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.lifetrack.ltapp.model.data.dclass.MenuItemData
import org.lifetrack.ltapp.ui.components.menuscreen.MenuListItem
import org.lifetrack.ltapp.ui.theme.LTAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavController) {
    val userName by remember { mutableStateOf("Dr. Najma") }
    val userEmail by remember { mutableStateOf( "najma@lifetrack.org") }
    val initials = userName.split(" ").map { it.first() }.joinToString("")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Menu",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowCircleLeft,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clickable { navController.navigate("profile") },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Column {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = userEmail,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        Icons.Filled.ChevronRight,
                        contentDescription = "Go to profile",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }

            items(getMenuItems()) { item ->
                MenuListItem(
                    icon = item.icon,
                    title = item.title,
                    onClick = {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                Row (
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        text = "Version 1.0.0",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


private fun getMenuItems() = listOf(
    MenuItemData("Health Records", Icons.Filled.MedicalServices, "medical_timeline"),
    MenuItemData("Medications", Icons.Filled.Medication, "additional_features"),
    MenuItemData("Appointments", Icons.Filled.CalendarToday, "telemedicine"),
    MenuItemData("Tele-Medicine", Icons.Filled.VideoCall, "telemedicine"),
    MenuItemData("Emergency Contacts", Icons.Filled.Emergency, "epidemic_alert"),
//    MenuItemData("Help & Support", Icons.AutoMirrored.Filled.Help, "support"),
    MenuItemData("About LifeTrack", Icons.Filled.Info, "about")
)

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun PreviewMenuScreen(){
    val navController = rememberNavController()
    LTAppTheme {
        MenuScreen(navController)
    }
}