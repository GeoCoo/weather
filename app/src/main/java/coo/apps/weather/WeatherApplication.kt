package coo.apps.weather

import android.app.Application
import com.google.android.libraries.places.api.Places

class WeatherApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize the SDK with the Google Maps Platform API key
        Places.initialize(this, BuildConfig.MAPS_API_KEY)
    }
}