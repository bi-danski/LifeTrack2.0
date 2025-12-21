package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lifetrack.ltapp.model.data.dclass.ToggleItemData
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.components.menuscreen.MenuListItem
import org.lifetrack.ltapp.ui.components.menuscreen.ToggleMenuListItem
import org.lifetrack.ltapp.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    userPresenter: UserPresenter,
    sharedPresenter: SharedPresenter
    ) {
    val userProfileInfo = userPresenter.profileInfo.collectAsState()

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
                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else Purple40,
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
                            text = userProfileInfo.value.userInitials,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column {
                        Text(
                            text = userProfileInfo.value.userName,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = userProfileInfo.value.userEmail,
                            fontWeight = FontWeight.SemiBold,
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

            item {
                ToggleMenuListItem(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                    toggleItem = ToggleItemData("Email Notifications", Icons.Default.Email),
                    onToggle = {
                        userPresenter.onEmailNotificationsUpdate()
                    },
                    toggleState = userPresenter.emailNotificationToggleState
                )
            }

            item {
                ToggleMenuListItem(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                    toggleItem = ToggleItemData("App Animations", Icons.Default.Animation),
                    onToggle = {
                        sharedPresenter.onAppAnimationsUpdate()
                    },
                    toggleState = sharedPresenter.appAnimationsToggleState
                )
            }

            item {
                ToggleMenuListItem(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                    toggleItem = ToggleItemData("App Notifications", Icons.Default.Notifications),
                    onToggle = {
                        userPresenter.onUserNotificationsUpdate()
                    },
                    toggleState = userPresenter.appNotificationToggleState
                )
            }

            item {
                ToggleMenuListItem(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                    toggleItem = ToggleItemData("Patient Data Consent", Icons.Filled.MedicalInformation),
                    onToggle = {
                        userPresenter.onPatientInfoConsentUpdate()
                    },
                    toggleState = userPresenter.patientInfoConsentToggleState
                )
            }

            items(userPresenter.menuItems) { item ->
                MenuListItem(
                    onClick = {
                        userPresenter.onMenuItemAction(
                            navController = navController,
                            route = item.route,
                        )
                    },
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                    menuItemData = item,
                )
            }
        }
//        Row (
//            verticalAlignment = Alignment.Bottom,
//            horizontalArrangement = Arrangement.Center,
//            modifier = Modifier
//                .fillMaxWidth()
//
//        ) {
//            Text(
//                text = "Version 1.0.0",
//                fontWeight = FontWeight.ExtraBold,
//                style = MaterialTheme.typography.labelSmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp),
//                textAlign = TextAlign.Center
//            )
//        }
    }
}
