package coo.apps.weather.network.controller

import android.location.Location
import coo.apps.weather.network.NetworkResponse
import coo.apps.weather.network.Service
import coo.apps.weather.network.request.HighChartsWaveRequest


class HighChartWaveController : Service() {

    suspend fun makeWaveRequest(location: Location?): Any? {
        val request = HighChartsWaveRequest(location)
        return when (val response = doRequest<Any>(request)) {
            is NetworkResponse.Success<*> -> response.result
            is NetworkResponse.Error -> response.error
        }
    }
}