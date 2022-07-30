package coo.apps.weather.network.controller

import android.location.Location
import coo.apps.weather.network.NetworkResponse
import coo.apps.weather.network.Service
import coo.apps.weather.network.request.HighChartsWaveRequest
import kotlinx.coroutines.runBlocking


class HighChartWaveController : Service() {

    fun makeWaveRequest(location: Location?): Any? {
        return runBlocking {
            val request = HighChartsWaveRequest(location)
            when (val response = doSuspendRequest<Any>(request)) {
//                is NetworkResponse.Success<*> -> return@runBlocking response.result
//                is NetworkResponse.Error -> return@runBlocking response.error
                else -> null
            }
        }
    }
}