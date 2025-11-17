package org.lifetrack.ltapp.view.ui.screens

//import com.example.lifetrack.ui.components.pulsate
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lifetrack.ltapp.view.components.homescreen.*

//import
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        floatingActionButton = {
            GlassFloatingActionButton(onClick = { navController.navigate("alma") }) {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Quick Chat")
            }
        },
        bottomBar = {
            AppBottomBar(navController)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            AppTopBar(navController)
            Spacer(Modifier.height(18.dp))
            TodayScheduleCard()
            Spacer(Modifier.height(18.dp))
            FeatureGrid(navController)
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    val navController = NavController(LocalContext.current)
    HomeScreen(navController)
}