package coo.apps.weather.network.controller

import android.location.Location
import coo.apps.weather.network.NetworkResponse
import coo.apps.weather.network.Service
import coo.apps.weather.network.request.HighChartsWeatherRequest
import kotlinx.coroutines.runBlocking

class HighChartsWeatherController : Service() {

    fun makeWeatherRequest(location: Location?): Any? {
        return runBlocking {
            val request = HighChartsWeatherRequest(location)
            when (val response = doSuspendRequest<Any>(request)) {
                is NetworkResponse.Success<*> -> return@runBlocking response.result
                is NetworkResponse.Error -> return@runBlocking response.error
                else -> null
            }
        }
    }
}


