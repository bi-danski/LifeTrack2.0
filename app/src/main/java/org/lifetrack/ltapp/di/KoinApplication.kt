package org.lifetrack.ltapp.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.koinConfiguration
import org.lifetrack.ltapp.core.localization.LocaleManager
import org.lifetrack.ltapp.core.localization.LocalizationProvider


@OptIn(KoinExperimentalAPI::class)
class KoinApplication: Application(), KoinStartup {

    override fun onCreate() {
        super.onCreate()
        LocaleManager.init(this)
        LocalizationProvider.setLocale(LocaleManager.getPreferredLanguage())
    }

    override fun onKoinStartup(): KoinConfiguration = koinConfiguration {
        androidContext(this@KoinApplication)
        androidLogger(Level.NONE)
        modules(KoinModule.koinModules)
        createEagerInstances()
//        analytics()
    }
}