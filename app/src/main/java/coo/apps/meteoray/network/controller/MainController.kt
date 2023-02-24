package coo.apps.meteoray.network.controller

import android.location.Location
import coo.apps.meteoray.models.main.MainResponse
import coo.apps.meteoray.network.NetworkResponse
import coo.apps.meteoray.network.Service
import coo.apps.meteoray.network.request.MainRequest


class MainController : Service() {

    suspend fun makeMainRequest(location: Location?,language:String): MainResponse? {
        val request = MainRequest(location,language)
        return when (val response = doRequest<MainResponse>(request)) {
            is NetworkResponse.Success<*> -> response.result as MainResponse?
            is NetworkResponse.Error -> null
        }
    }
}