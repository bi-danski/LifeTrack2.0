package org.lifetrack.ltapp.view.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lifetrack.ltapp.view.components.aboutscreen.FeatureItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    val colorScheme = MaterialTheme.colorScheme
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "", //  "About LifeTrack",
                        style = MaterialTheme.typography.headlineSmall.copy()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowCircleLeft,
                            contentDescription = "Back",
                            tint = Color(0xFF2E5EAA)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background, //colorScheme.primaryContainer,
                    titleContentColor = colorScheme.onPrimaryContainer,
                    navigationIconContentColor = colorScheme.onPrimaryContainer
                )
            )
        },
        containerColor = colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4A90E2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = "App Icon",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "LifeTrack Health Companion",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFF2E5EAA),
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your complete health management solution",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF6C757D),
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.background
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "About LifeTrack",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color(0xFF2E5EAA)
                        )
                    )
                    Text(
                        text = "LifeTrack is a cutting-edge, comprehensive health management app designed to empower you with seamless control over your medical history, appointments, and wellness journey.",
                        style = MaterialTheme.typography.bodyMedium
                    )

//                    HorizontalDivider(
//                        modifier = Modifier.padding(vertical = 8.dp),
//                        thickness = 1.dp,
//                        color = Color(0xFFE9ECEF)
//                    )

                    Text(
                        text = "Key Features",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color(0xFF2E5EAA)
                        )
                    )

                    FeatureItem(
                        iconColor = Color(0xFF4A90E2),
                        title = "Medical Timeline & Records",
                        description = "Organize and access your medical history with ease"
                    )
                    FeatureItem(
                        iconColor = Color(0xFF4A90E2),
                        title = "Telemedicine Appointments",
                        description = "Connect with healthcare professionals from home"
                    )
                    FeatureItem(
                        iconColor = Color(0xFF4A90E2),
                        title = "Medication Reminders",
                        description = "Never miss a dose with customizable alerts"
                    )
                    FeatureItem(
                        iconColor = Color(0xFF4A90E2),
                        title = "Emergency Alerts",
                        description = "Instantly notify contacts in urgent situations"
                    )
                    FeatureItem(
                        iconColor = Color(0xFF4A90E2),
                        title = "Health Information Hub",
                        description = "Access reliable health resources and articles"
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = Color(0xFFE9ECEF)
                    )

                    Text(
                        text = "Developed By",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color(0xFF2E5EAA)
                        )
                    )
                    Text(
                        text = "LifeTrack Health Solutions",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Innovating for Your Well-Being",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF6C757D)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = colorScheme.background,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Contact Us:",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color(0xFF2E5EAA)
                            )
                        )
                        Text(
                            text = "contact@lifetrack.org",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "www.lifetrack.org",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
//
                }
            }
            Text(
                text = "        © 2023 LifeTrack. All rights reserved.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF6C757D)
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

