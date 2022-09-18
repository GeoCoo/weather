package coo.apps.weather.network.controller

import android.location.Location
import coo.apps.weather.models.main.MainResponse
import coo.apps.weather.network.NetworkResponse
import coo.apps.weather.network.Service
import coo.apps.weather.network.request.MainRequest


class MainController : Service() {

    suspend fun makeMainRequest(location: Location?): MainResponse? {
        val request = MainRequest(location)
        return when (val response = doRequest<MainResponse>(request)) {
            is NetworkResponse.Success<*> -> response.result as MainResponse?
            is NetworkResponse.Error -> null
        }
    }
}