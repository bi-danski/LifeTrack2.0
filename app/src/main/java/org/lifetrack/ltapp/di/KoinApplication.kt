package org.lifetrack.ltapp.di

import android.app.Application
import io.kotzilla.sdk.analytics.koin.analytics
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.lifetrack.ltapp.BuildConfig

class KoinApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if(BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@KoinApplication)
            modules(koinModule)
            analytics()
        }
    }

}