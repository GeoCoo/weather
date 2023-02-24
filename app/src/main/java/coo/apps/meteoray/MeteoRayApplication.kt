package coo.apps.meteoray

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MeteoRayApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MeteoRayApplication)
            modules(KoinModule(this@MeteoRayApplication).modules)
        }
    }
}