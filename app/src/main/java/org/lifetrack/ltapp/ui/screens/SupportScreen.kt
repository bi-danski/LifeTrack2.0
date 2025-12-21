package org.lifetrack.ltapp.ui.screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.ContactSupport
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.ui.components.supportscreen.ContactItem
import org.lifetrack.ltapp.ui.components.supportscreen.FAQItem
import org.lifetrack.ltapp.ui.components.supportscreen.SectionCard
import org.lifetrack.ltapp.utils.openDialer
import org.lifetrack.ltapp.utils.openEmail


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    navController: NavController,
    presenter: SharedPresenter
    ) {
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
                            Icons.Default.ArrowCircleLeft,
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
            contentPadding = PaddingValues(horizontal = 5.dp, vertical = 10.dp)
        ) {
            item {
                SectionCard(
                    title = "Frequently Asked Questions",
                    icon = Icons.AutoMirrored.Filled.HelpOutline
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    FAQItem(
                        question = "How do I reset my password?",
                        answer = "Go to Profile > Settings > Reset Password.",
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FAQItem(
                        question = "How to contact a doctor?",
                        answer = "Use the Telemedicine feature on your home screen.",
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FAQItem(
                        question = "Where can I view my medical records?",
                        answer = "Tap Medical Timeline on the home screen.",
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FAQItem(
                        question = "How do I update my profile information?",
                        answer = "Navigate to Profile > Edit Profile to update your details.",
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FAQItem(
                        question = "What should I do in an emergency?",
                        answer = "Use the Emergency Alert button on the home screen to notify your emergency contacts.",
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
                        value = "support@lifetrack.org",
                        onClick = { context.openEmail("support@lifetrack.app") }
                    )
                    ContactItem(
                        icon = Icons.Default.Phone,
                        label = "Phone Support",
                        value = "+254 790 938 365",
                        onClick = { context.openDialer("+254790038365") }
                    )
                    ContactItem(
                        icon = Icons.AutoMirrored.Default.Chat,
                        label = "Live Chat",
                        value = "Available 24/7",
                        onClick = { }
                    )
                }
            }

            item {
                SectionCard(
                    title = "About Patient Data Consent",
                    icon = Icons.Filled.Info
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "In this case you allow Alma A.I Assistant and the Doctors assigned to have access to all your health records information, for better guidance and treatment",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            text = "Version",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                        Text(
//                            text = presenter.version,
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurface
//                        )
//                    }
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