package coo.apps.weather.network

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.ResponseResultOf
import com.github.kittinunf.fuel.core.extensions.authentication
import com.google.gson.Gson
import coo.apps.weather.network.ServiceException.JsonDesiriazion
import coo.apps.weather.network.request.BaseRequest
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.Charset

sealed class ServiceException : Exception() {
    data class JsonDesiriazion(val messageDesc: String, val e: Exception? = null) : Exception()
    class UnAuthorizedException : Exception()
    class RedirectException : Exception()
    class AccountException : Exception()
    class IntervalError : Exception()
    class TimeOutException : Exception()
}

sealed class NetworkResponse {
    data class Success<out T>(val result: T) : NetworkResponse()
    data class Error(val error: java.lang.Exception) : NetworkResponse()

}

enum class ResponseStatus(val code: Int) {
    OK(200),
    UnAuth(400),
    Redirect(300),
    AccountException(600),
    IntervalError(500)
}

abstract class Service {

    val timeOutMilisTime: Int = 20 * 1000
    protected var disposable: CompositeDisposable = CompositeDisposable()
    private var gSon: Gson = Gson()

    //    @Throws(Exception::class)
    suspend inline fun <reified T> doSuspendRequest(request: BaseRequest): NetworkResponse {
        return withContext(Dispatchers.IO) {
            var localResponse: ResponseResultOf<String>? = null
            localResponse = if (request.method == BaseRequest.Method.GET) {
                when {
                    request.queryParameter.isNullOrEmpty() -> Fuel.get(request.baseUrl + request.path, request.defaultUrlParams?.toList())
                        .header(request.header ?: request.defaultHeaders)
                        .authentication().basic("aramco", "aramco-W!")
                        .timeoutRead(timeOutMilisTime)
                        .responseString()
                    else -> Fuel.get(request.baseUrl + request.path, request.queryParameter!!.toList())
                        .authentication().basic("aramco", "aramco-W!")
                        .header(request.header ?: request.defaultHeaders)
                        .timeoutRead(timeOutMilisTime)
                        .responseString()
                }
            } else {
                when (request.body) {
                    null -> Fuel.post(
                        request.baseUrl + request.path,
                        request.defaultUrlParams?.toList()
                    )
                        .header(request.header ?: request.defaultHeaders)
                        .timeoutRead(timeOutMilisTime)
                        .responseString()
                    else -> Fuel.post(
                        request.baseUrl + request.path,
                        request.defaultUrlParams?.toList()
                    )
                        .header(request.header ?: request.defaultHeaders)
                        .timeoutRead(timeOutMilisTime)
                        .body(request.body!!, Charset.forName("UTF-8"))
                        .responseString()
                }
            }
            val statusCode = localResponse.second.statusCode
            if (statusCode == ResponseStatus.UnAuth.code) {
                return@withContext NetworkResponse.Error(ServiceException.UnAuthorizedException())
            }
            if (statusCode == ResponseStatus.Redirect.code) {
                return@withContext NetworkResponse.Error(ServiceException.RedirectException())
            }
            if (statusCode == ResponseStatus.AccountException.code) {
                return@withContext NetworkResponse.Error(ServiceException.AccountException())
            }
            if (statusCode == ResponseStatus.IntervalError.code) {
                return@withContext NetworkResponse.Error(ServiceException.IntervalError())
            }
            val (payload, error) = localResponse.third
            if (error?.isTimeOut() == true) {
                return@withContext NetworkResponse.Error(ServiceException.TimeOutException())
            }
            try {
                val modelDesiriazed = Gson().fromJson(payload, T::class.java)
                if (modelDesiriazed != null) {
                    return@withContext NetworkResponse.Success(modelDesiriazed)
                } else {
                    val exception = JsonDesiriazion("Deserialzed error at ${T::class.java.name}")
                    return@withContext NetworkResponse.Error(exception)
                }
            } catch (e: Exception) {
                val exception = JsonDesiriazion("Deserialzed error at ${T::class.java.name}", e)
                return@withContext NetworkResponse.Error(exception)
            }
        }
    }
}

fun FuelError.isTimeOut(): Boolean {
    return this.exception.message == "timeout"
}
