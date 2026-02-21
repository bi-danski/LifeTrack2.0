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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lifetrack.ltapp.R
import org.lifetrack.ltapp.model.data.dclass.MenuItemData
import org.lifetrack.ltapp.model.data.dclass.ToggleItemData
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.ui.components.menuscreen.MenuListItem
import org.lifetrack.ltapp.ui.components.menuscreen.ToggleMenuListItem
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import org.lifetrack.ltapp.ui.theme.Purple40


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(authPresenter: AuthPresenter, sharedPresenter: SharedPresenter) {
    val userProfileInfo = authPresenter.profileInfo.collectAsStateWithLifecycle()
    val ltSettings = sharedPresenter.ltSettings.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.menu),
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { LTNavDispatch.navigateBack() }) {
                        Icon(
                            Icons.Default.ArrowCircleLeft,
                            contentDescription = null,
                            tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background
                    else Purple40, //MaterialTheme.colorScheme.primaryContainer,
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
                        .clickable { LTNavDispatch.navigate("profile") },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primaryContainer else Purple40),
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
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                ToggleMenuListItem(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                    toggleItem = ToggleItemData(stringResource(R.string.email_notifications), Icons.Default.Email),
                    onToggle = { sharedPresenter.onEmailNotificationsUpdate() },
                    toggleState = ltSettings.value.emailNotifications
                )
            }

            item {
                ToggleMenuListItem(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                    toggleItem = ToggleItemData(stringResource(R.string.in_app_animations), Icons.Default.Animation),
                    onToggle = { sharedPresenter.onAppAnimationsUpdate() },
                    toggleState = ltSettings.value.animations
                )
            }

            item {
                ToggleMenuListItem(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                    toggleItem = ToggleItemData(stringResource(R.string.caro_animations), Icons.Default.Animation),
                    onToggle = { sharedPresenter.onAppCarouselAnimationsUpdate() },
                    toggleState = ltSettings.value.carouselAutoRotate
                )
            }

            item {
                ToggleMenuListItem(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                    toggleItem = ToggleItemData(stringResource(R.string.app_notifications), Icons.Default.Notifications),
                    onToggle = { sharedPresenter.onUserNotificationsUpdate() },
                    toggleState = ltSettings.value.notifications
                )
            }

            item {
                ToggleMenuListItem(
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                    toggleItem = ToggleItemData(stringResource(R.string.pdc), Icons.Filled.MedicalInformation),
                    onToggle = { sharedPresenter.onPatientInfoConsentUpdate() },
                    toggleState = ltSettings.value.dataConsent
                )
            }

            item {
                MenuListItem(
                    onClick = { LTNavDispatch.navigate("epidemic_alert") },
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                    menuItemData = MenuItemData(stringResource(R.string.emergency_contact),
                        Icons.Filled.Emergency,
                        rightIcon = null
                    )
                )
            }

            item {
                MenuListItem(
                    onClick = { LTNavDispatch.navigate("about") },
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40,
                    menuItemData = MenuItemData(stringResource(R.string.about_lt),
                        Icons.Filled.Info,
                        rightIcon = null,
                    )
                )
            }
        }
    }
}
