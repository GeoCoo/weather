package coo.apps.weather.network.controller

import coo.apps.weather.models.Limits
import coo.apps.weather.network.NetworkResponse
import coo.apps.weather.network.Service
import coo.apps.weather.network.request.LimitRequest

class LimitController : Service() {

    suspend fun makeLimitRequest(): Limits? {
        val request = LimitRequest()
        return when (val response = doRequest<Limits>(request)) {
            is NetworkResponse.Success<*> -> response.result as Limits
            is NetworkResponse.Error -> null
        }
    }
}