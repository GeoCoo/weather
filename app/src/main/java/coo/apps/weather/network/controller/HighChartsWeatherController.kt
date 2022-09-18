package coo.apps.weather.network.controller

import android.location.Location
import coo.apps.weather.models.weather.WeatherResponse
import coo.apps.weather.network.NetworkResponse
import coo.apps.weather.network.Service
import coo.apps.weather.network.request.HighChartsWeatherRequest

class HighChartsWeatherController : Service() {

    suspend fun makeWeatherRequest(location: Location?): WeatherResponse? {
        val request = HighChartsWeatherRequest(location)
        return when (val response = doRequest<WeatherResponse>(request)) {
            is NetworkResponse.Success<*> -> response.result as WeatherResponse?
            is NetworkResponse.Error -> null
        }
    }
}


