package org.lifetrack.ltapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.lifetrack.ltapp.core.notification.DroidNotification
import org.lifetrack.ltapp.model.data.dclass.SessionStatus
import org.lifetrack.ltapp.presenter.SessionManager
import org.lifetrack.ltapp.ui.navigation.LTNavDispatcher
import org.lifetrack.ltapp.ui.navigation.LTNavTarget
import org.lifetrack.ltapp.ui.navigation.LTNavigation
import org.lifetrack.ltapp.ui.theme.LTAppTheme

class MainActivity : ComponentActivity() {
    private val sessionManager: SessionManager by inject()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            sessionManager.sessionState.value == SessionStatus.INITIALIZING
        }
        lifecycleScope.launch(Dispatchers.IO) {
            DroidNotification.createNotificationChannel(this@MainActivity)
        }

        setContent {
            LTAppTheme {
                val sessionStatus by sessionManager.sessionState.collectAsState()
                val appNavController = rememberNavController()

                LaunchedEffect(appNavController) {
                    LTNavDispatcher.navigationEvents.collect { target ->
                        when (target) {
                            is LTNavTarget.Screen -> {
                                appNavController.navigate(target.route) {
                                    if (target.clearBackstack) {
                                        appNavController.graph.startDestinationRoute?.let { route ->
                                            popUpTo(route) { inclusive = true }
                                        }
                                    }
                                    launchSingleTop = target.launchSingleTop
                                }
                            }
                            is LTNavTarget.Back -> {
                                if (!appNavController.popBackStack()) {
                                    finish()
                                }
                            }
                        }
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    when (sessionStatus) {
                        SessionStatus.LOGGED_IN -> {
                            LTNavigation(
                                navController = appNavController,
                                startDestination = "home_graph"
                            )
                        }
                        SessionStatus.LOGGED_OUT -> {
                            LTNavigation(
                                navController = appNavController,
                                startDestination = "auth_graph"
                            )
                        }
                        else -> { }
                    }
                }
            }
        }
    }
}