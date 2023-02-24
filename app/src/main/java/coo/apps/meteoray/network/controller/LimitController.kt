package coo.apps.meteoray.network.controller

import coo.apps.meteoray.models.Limits
import coo.apps.meteoray.network.NetworkResponse
import coo.apps.meteoray.network.Service
import coo.apps.meteoray.network.request.LimitRequest

class LimitController : Service() {

    suspend fun makeLimitRequest(): Limits? {
        val request = LimitRequest()
        return when (val response = doRequest<Limits>(request)) {
            is NetworkResponse.Success<*> -> response.result as Limits
            is NetworkResponse.Error -> null
        }
    }
}