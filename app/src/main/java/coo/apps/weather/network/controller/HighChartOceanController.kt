package coo.apps.weather.network.controller

import android.location.Location
import coo.apps.weather.network.NetworkResponse
import coo.apps.weather.network.Service
import coo.apps.weather.network.request.HighChartsOceanRequest

class HighChartOceanController : Service() {

    suspend fun makeOceanRequest(location: Location?): Any? {
        val request = HighChartsOceanRequest(location)
        return when (val response = doRequest<Any>(request)) {
            is NetworkResponse.Success<*> -> return response.result
            is NetworkResponse.Error -> null
        }
    }
}