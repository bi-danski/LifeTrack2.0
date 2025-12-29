package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.ui.components.aboutscreen.FeatureItem
import org.lifetrack.ltapp.ui.components.supportscreen.SectionCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController,
    sharedPresenter: SharedPresenter
) {
    val colorScheme = MaterialTheme.colorScheme
    val thatOneColor = Color(0xFF6C757D)
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
//                            tint = Color(0xFF2E5EAA)
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
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(0xFF2E5EAA),
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
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(0.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SectionCard(
                        title = "About LifeTrack",
                        icon = Icons.Filled.Info
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "LifeTrack is your digital health companion app providing telemedicine, health tracking, and emergency alerts. Our mission is to empower patients with accessible health data and real-time medical assistance.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Version",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = sharedPresenter.version,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

//                    Text(
//                        text = "LifeTrack is a cutting-edge, comprehensive health management app designed to empower you with seamless control over your medical history, appointments, and wellness journey.",
//                        style = MaterialTheme.typography.bodyMedium,
////                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else thatOneColor,
//                        fontWeight = FontWeight.SemiBold,
//                        color = thatOneColor,
//                        fontSize = 16.sp
//                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = colorScheme.background
                    )
                    Text(
                        text = "Key Features",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(0xFF2E5EAA)
                        )
                    )

                    FeatureItem(
                        iconColor =if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(0xFF4A90E2),
                        title = "Medical Timeline & Records",
                        description = "Organize and access your medical history with ease"
                    )
                    FeatureItem(
                        iconColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(0xFF4A90E2),
                        title = "Telemedicine Appointments",
                        description = "Connect with healthcare professionals from home"
                    )
                    FeatureItem(
                        iconColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(0xFF4A90E2),
                        title = "Medication Reminders",
                        description = "Never miss a dose with customizable alerts"
                    )
                    FeatureItem(
                        iconColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(0xFF4A90E2),
                        title = "Emergency Alerts",
                        description = "Instantly notify contacts in urgent situations"
                    )
                    FeatureItem(
                        iconColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(0xFF4A90E2),
                        title = "Health Information Hub",
                        description = "Access reliable health resources and articles"
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = colorScheme.background
                    )

                    Text(
                        text = "Developed By",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(0xFF2E5EAA)
                        )
                    )
                    Text(
                        text = "LifeTrack Health Solutions",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = thatOneColor
                        )
                    )
                    Text(
                        text = "Innovating for Your Well-Being",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.SemiBold,
                            color = thatOneColor
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = colorScheme.background,
                                shape = MaterialTheme.shapes.medium
                            ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Contact Us:",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Color(0xFF2E5EAA)
                            )
                        )
                        Text(
                            text = "contact@lifetrack.org",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = thatOneColor
                        )
                        Text(
                            text = "www.lifetrack.org",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = thatOneColor
                        )
                        Spacer(modifier = Modifier.height(15.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ){
                        Text(
                            text = "© 2023 LifeTrack. All rights reserved.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF6C757D),
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

