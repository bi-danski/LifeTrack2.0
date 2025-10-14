package org.lifetrack.ltapp.view.ui.screens.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.HelpCenter
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.MultilineChart
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
//import com.example.lifetrack.ui.components.pulsate
import org.lifetrack.ltapp.view.theme.Purple40
import org.lifetrack.ltapp.view.theme.Purple80


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        floatingActionButton = {
            GlassFloatingActionButton(onClick = { /* Quick chat */ }) {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Quick Chat")
            }
        },
        bottomBar = { DashboardBottomBar() },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            DashboardHeader(navController)
            Spacer(Modifier.height(18.dp))
            TodayScheduleCard()
            Spacer(Modifier.height(18.dp))
            FeatureGrid(navController)
        }
    }
}

//@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun DashboardHeader(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .border(width = 2.dp, color = MaterialTheme.colorScheme.primary, shape = CircleShape),
                contentAlignment = Alignment.Center

            ) {
                IconButton(onClick = { navController.navigate("menu") },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background))
                {
                    Icon(Icons.Filled.Menu, contentDescription = "") }
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = "LIFETRACK",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("Dr. Najma",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                )
            Text("Patient",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TodayScheduleCard() {
    GlassCard(shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
//            .background(color = MaterialTheme.colorScheme.surfaceVariant)
//            .pulsate()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Today's Schedule",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier
                ) {
                    Text(
                        textAlign = TextAlign.End,
                        text = "7",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                    )
                }
                    Spacer(Modifier.height(4.dp))

                Text(text = "Appointments",
//                    fontSize = 64.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40)

            }


            Column(modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(modifier = Modifier.padding(bottom = 10.dp)){
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications,
                            contentDescription = "Upcoming",
                            modifier = Modifier
                                .size(45.dp)
                            )
                    }
                    Text(
                        text = "14",
                        textAlign = TextAlign.Right,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Upcoming",
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                )
            }
        }
    }
}

@Composable
private fun FeatureGrid(navController: NavController) {
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

@Composable
private fun DashboardBottomBar() {
    var selected by remember { mutableIntStateOf(0) }
    val shape = RoundedCornerShape(24.dp)
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Purple40.copy(0.4f),
                        Purple40.copy(0.4f),
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        Purple40.copy(0.4f)
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    )
                ),
                shape = shape
            ),
        containerColor = Color.Transparent
    ) {
        NavigationBarItem(
            selected = selected == 0,
            onClick = { selected = 0 },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selected == 2,
            onClick = { selected = 2 },
            icon = { Icon(Icons.Filled.BarChart, contentDescription = "Reports") },
            label = { Text("Medical Records", textAlign = TextAlign.Justify) }
        )
        NavigationBarItem(
            selected = selected == 1,
            onClick = { selected = 1 },
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}

@Composable
private fun GlassActionCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    val shape: Shape = RoundedCornerShape(22.dp)
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
//            .width(170.dp)
            .clip(shape)
            .background(
                color = MaterialTheme.colorScheme.background.copy(0.08f),
//                brush = Brush.verticalGradient(
//                    listOf(
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
//                    )
//                ),
//                shape = shape
            ),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.primary.copy(0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(if(isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon,
                    contentDescription = title,
                    tint = if(isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary else Purple80
                )
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = if(isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                maxLines = 2,
                fontWeight = FontWeight.Bold
//                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun GlassCard(shape: Shape, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
//                        Purple40.copy(0.4f),
                        Purple80.copy(0.05f),
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    )
                ),
                shape = shape
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        content = { Column(content = content) }
    )
}

@Composable
private fun GlassFloatingActionButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    val shape = CircleShape
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color.Transparent,
        contentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
        modifier = Modifier
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Purple80.copy(0.5f),
                        Purple80.copy(alpha = 0.5f)
                    )
                ),
                shape = shape
            )
    ) {
        content()
    }
}
