package org.lifetrack.ltapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
//import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//import org.koin.androidx.viewmodel.ext.android.viewModel
import org.lifetrack.ltapp.core.notification.DroidNotification
//import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.ui.navigation.AppNavigation
import org.lifetrack.ltapp.ui.theme.LTAppTheme


class MainActivity : ComponentActivity() {
//    private val authPresenter: AuthPresenter by viewModel()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        val start = System.currentTimeMillis()
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        println("DEBUG: super.onCreate took ${System.currentTimeMillis() - start}ms")

        lifecycleScope.launch(Dispatchers.IO) {
            DroidNotification.createNotificationChannel(this@MainActivity)
        }

        println("DEBUG: Until Content Creation  ${System.currentTimeMillis() - start}ms")

        setContent {
            LTAppTheme {
//                val sessionStatus by authPresenter.sessionState.collectAsStateWithLifecycle()
                Surface(modifier = Modifier.fillMaxSize()) {
                    val appNavController = rememberNavController()

//                    if (sessionStatus != SessionStatus.INITIALIZING) {
                        AppNavigation(
                            navController = appNavController,
//                            sessionStatus = sessionStatus
                        )
//                    }
                }
            }
        }

        println("DEBUG: Until End of Content Creation  ${System.currentTimeMillis() - start}ms")
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}