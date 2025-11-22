package org.lifetrack.ltapp.di

import android.app.Application
import org.koin.core.context.startKoin
//import android

class KoinApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
//            androidContext(this@KoinApplication)
            modules(koinModule)
        }
    }

}