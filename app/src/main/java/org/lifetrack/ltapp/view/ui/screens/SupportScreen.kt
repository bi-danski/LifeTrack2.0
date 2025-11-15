package org.lifetrack.ltapp.view.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.lifetrack.ltapp.utils.openDialer
import org.lifetrack.ltapp.utils.openEmail
import org.lifetrack.ltapp.view.components.supportscreen.ContactItem
import org.lifetrack.ltapp.view.components.supportscreen.FAQItem
import org.lifetrack.ltapp.view.components.supportscreen.SectionCard
import org.lifetrack.ltapp.view.ui.theme.LTAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Help & Support",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp)
        ) {
            item {
                SectionCard(
                    title = "Frequently Asked Questions",
                    icon = Icons.AutoMirrored.Filled.HelpOutline
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    FAQItem(
                        question = "How do I reset my password?",
                        answer = "Go to Profile > Settings > Reset Password."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FAQItem(
                        question = "How to contact a doctor?",
                        answer = "Use the Telemedicine feature on your home screen."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FAQItem(
                        question = "Where can I view my medical records?",
                        answer = "Tap Medical Timeline on the home screen."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FAQItem(
                        question = "How do I update my profile information?",
                        answer = "Navigate to Profile > Edit Profile to update your details."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FAQItem(
                        question = "What should I do in an emergency?",
                        answer = "Use the Emergency Alert button on the home screen to notify your emergency contacts."
                    )
                }
            }

            item {
                SectionCard(
                    title = "Contact Us",
                    icon = Icons.AutoMirrored.Filled.ContactSupport
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    ContactItem(
                        icon = Icons.Default.Email,
                        label = "Email Support",
                        value = "support@lifetrack.app",
                        actionLabel = "Send Email",
                        onClick = { context.openEmail("support@lifetrack.app") }
                    )
                    ContactItem(
                        icon = Icons.Default.Phone,
                        label = "Phone Support",
                        value = "+254 790 038 365",
                        actionLabel = "Call Now",
                        onClick = { context.openDialer("+254790038365") }
                    )
                    ContactItem(
                        icon = Icons.AutoMirrored.Default.Chat,
                        label = "Live Chat",
                        value = "Available 24/7",
                        actionLabel = "Start Chat",
                        onClick = { }
                    )
                }
            }

            item {
                SectionCard(
                    title = "About LifeTrack",
                    icon = Icons.Filled.Info
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "LifeTrack is your digital health companion app providing telemedicine, health tracking, and emergency alerts. Our mission is to empower patients with accessible health data and real-time medical assistance.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                            text = "1.0.0",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            item {
                Text(
                    text = "© 2023 LifeTrack Health Inc.\nAll rights reserved",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun PreviewSupportScreen(){
    val navController = rememberNavController()
    LTAppTheme {
        SupportScreen(
            navController = navController,
        )
    }

}