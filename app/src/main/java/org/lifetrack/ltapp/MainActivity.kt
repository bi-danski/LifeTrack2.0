package org.lifetrack.ltapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.lifetrack.ltapp.core.localization.LocaleManager
import org.lifetrack.ltapp.core.localization.LocalizationProvider
import org.lifetrack.ltapp.core.notification.DroidNotification
import org.lifetrack.ltapp.model.data.dclass.SessionStatus
import org.lifetrack.ltapp.network.NetworkObserver
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.service.SessionManager
import org.lifetrack.ltapp.ui.theme.LTAppTheme

class MainActivity : AppCompatActivity() {
    private val sessionManager: SessionManager by inject()
    private val networkObserver: NetworkObserver by inject()
    private val sharedPresenter: SharedPresenter by inject()

    override fun attachBaseContext(newBase: Context) {
        LocaleManager.init(newBase)
        val lang = LocaleManager.getPreferredLanguage()
        val context = LocalizationProvider.getLocalizedContext(newBase, lang)
        super.attachBaseContext(context)
    }

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
            val isOnline by networkObserver.isConnected.collectAsStateWithLifecycle()
            val sessionStatus by sessionManager.sessionState.collectAsState()
            val currentLanguage by sharedPresenter.currentLanguage.collectAsStateWithLifecycle()
            val lastAppliedLanguage = rememberSaveable { mutableStateOf(currentLanguage) }

            LaunchedEffect(currentLanguage) {
                if (lastAppliedLanguage.value != currentLanguage) {
                    val appliedInPlace = LocalizationProvider.setLocale(this@MainActivity, currentLanguage)
                    LocaleManager.setPreferredLanguage(currentLanguage)
                    lastAppliedLanguage.value = currentLanguage
                    if (!appliedInPlace) {
                        recreate()
                    }
                }
            }

            LTAppTheme {
                RootContent(
                    isOnline = isOnline,
                    sessionStatus = sessionStatus
                )
            }
        }
    }
}