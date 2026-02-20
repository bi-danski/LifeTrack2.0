package org.lifetrack.ltapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
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
        val localizedContext = LocalizationProvider.getLocalizedContext(newBase, lang)
        super.attachBaseContext(localizedContext)
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedPresenter.ltSettings
                    .map { it.preferredLanguage }
                    .distinctUntilChanged()
                    .drop(1)
                    .collect { newLang ->
                        if (newLang.isNotEmpty()) {
                            val currentResourcesLang = resources.configuration.locales[0].language
                            if (newLang != currentResourcesLang) {
                                LocaleManager.setPreferredLanguage(newLang)
                                LocalizationProvider.setLocale(newLang)
                                runOnUiThread { recreate() }
                            }
                        }
                    }
            }
        }

        setContent {
            val isOnline by networkObserver.isConnected.collectAsStateWithLifecycle()
            val sessionStatus by sessionManager.sessionState.collectAsStateWithLifecycle()

            LTAppTheme {
                RootContent(
                    isOnline = isOnline,
                    sessionStatus = sessionStatus
                )
            }
        }
    }
}