package org.lifetrack.ltapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.lifetrack.ltapp.core.notifications.DroidNotification
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.ui.navigation.AppNavigation
import org.lifetrack.ltapp.ui.theme.LTAppTheme

class MainActivity : ComponentActivity() {
    private val authPresenter: AuthPresenter by viewModel()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashState = installSplashScreen()
        var isLoaded = false
        super.onCreate(savedInstanceState)
        DroidNotification.createNotificationChannel(this)
        lifecycleScope.launch {
            authPresenter.sessionState.collect {
                isLoaded = true
            }
        }
        splashState.setKeepOnScreenCondition { !isLoaded }
        enableEdgeToEdge()
        setContent {
            LTAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val appNavController = rememberNavController()
                    AppNavigation(appNavController)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}