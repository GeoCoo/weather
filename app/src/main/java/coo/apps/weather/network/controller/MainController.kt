package coo.apps.weather.network.controller

import android.location.Location
import coo.apps.weather.network.NetworkResponse
import coo.apps.weather.network.request.MainRequest
import kotlinx.coroutines.runBlocking


class MainController() : BaseController() {

//    private lateinit var request: MainRequest
//    var response: PublishSubject<Any> = PublishSubject.create()


    fun makeMainRequest(location: Location?): Any? {
        return runBlocking{
            val request = MainRequest(location)
            when (val response = doSuspendRequest<Any>(request)) {
                is NetworkResponse.Success<*> -> return@runBlocking response.result
                is NetworkResponse.Error -> return@runBlocking  response.error
                else -> null
            }
        }
    }

//    override suspend fun fetchData() {
//        super.fetchData()
//        val singleDisposable = doSuspendRequest<Any>(request).subscribe { data, error ->
//            if (error != null) {
//                handleException(error)
//                return@subscribe
//            }
//            response.onNext(data)
//        }
//        disposable.add(singleDisposable)
//    }


//    fun initRequest(location: Location) {
//        request = MainRequest(location)
//    }
}