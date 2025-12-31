package org.lifetrack.ltapp.di

import android.app.Application
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
        }
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                koinApp.koin.get<LTRoomDatabase>()
//                koinApp.koin.get<io.ktor.client.HttpClient>()
//            } catch (e: Exception) {
//                android.util.Log.e("KoinApp", "Background warm-up failed", e)
//            }
//        }
    }

}