package org.lifetrack.ltapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.core.utility.openDialer
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.HomePresenter
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.components.homescreen.carousels.LtHomeCarousel
import org.lifetrack.ltapp.ui.components.homescreen.AppBottomBar
import org.lifetrack.ltapp.ui.components.homescreen.AppTopBar
import org.lifetrack.ltapp.ui.components.homescreen.GlassFloatingActionButton
import org.lifetrack.ltapp.ui.components.homescreen.featureGridContent
import org.lifetrack.ltapp.ui.state.UIState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homePresenter: HomePresenter,
    userPresenter: UserPresenter,
    authPresenter: AuthPresenter,
    sharedPresenter: SharedPresenter
) {
    val autoRotate2NextCard = homePresenter.autoRotate2NextCard
    val caroItemsCount = homePresenter.caroItemsCount
    val userInfo = authPresenter.profileInfo.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val authUiState by authPresenter.uiState.collectAsStateWithLifecycle(UIState.Idle)
    val homeScreenContextInstance = LocalContext.current
    val hapticFeedbackContextInstance = LocalHapticFeedback.current
    val scope = rememberCoroutineScope ()

    val callPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        permissionGrantedOrNot ->
        if(permissionGrantedOrNot) {
            sharedPresenter.handleEmergencyCall(homeScreenContextInstance)
        }else{
            homeScreenContextInstance.openDialer(phone = "911")
        }
    }

    LaunchedEffect(authUiState) {
        when (authUiState) {
            is UIState.Success -> {
                val msg = (authUiState as UIState.Success).message
                if (!msg.isNullOrBlank()) {
                    snackbarHostState.showSnackbar(msg)
                    authPresenter.resetUIState()
                }
            }

            is UIState.Error -> {
                val errorMsg = (authUiState as UIState.Error).msg
                snackbarHostState.showSnackbar(
                    message = errorMsg,
                    duration = SnackbarDuration.Long
                )
                authPresenter.resetUIState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                val isError = authUiState is UIState.Error
                Snackbar(
                    snackbarData = data,
                    containerColor = if (isError)
                        MaterialTheme.colorScheme.errorContainer
                    else
                        MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (isError)
                        MaterialTheme.colorScheme.onErrorContainer
                    else
                        MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        floatingActionButton = {
            GlassFloatingActionButton(onClick = { navController.navigate("alma") }) {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Quick Chat")
            }
        },
        bottomBar = { AppBottomBar() },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Spacer(Modifier.height(8.dp))
                    AppTopBar(userInfo.value.userName)
                    Spacer(Modifier.height(18.dp))
                    LtHomeCarousel(
                        autoRotate = autoRotate2NextCard,
                        itemsCount = caroItemsCount,
                        userPresenter = userPresenter,
                        onEmergencyClickAction = {
                            hapticFeedbackContextInstance.performHapticFeedback(
                                HapticFeedbackType.LongPress
                            )
                            val permissionCheck = ContextCompat.checkSelfPermission(
                                homeScreenContextInstance,
                                Manifest.permission.CALL_PHONE
                            )
                            if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
                                sharedPresenter.handleEmergencyCall(
                                    homeScreenContextInstance
                                )
                            }else{
                                callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                            }
                        },
                        onEmergencyContactClickAction = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Coming Soon. Stay Tuned")
                            }
                        }
                    )
                    Spacer(Modifier.height(18.dp))
                }
            }

            featureGridContent(navController)

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
