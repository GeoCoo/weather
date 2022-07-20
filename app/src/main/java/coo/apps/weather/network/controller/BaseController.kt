package coo.apps.weather.network.controller


import com.github.kittinunf.fuel.core.FuelError
import coo.apps.weather.network.ServerException
import coo.apps.weather.network.Service
import coo.apps.weather.network.streamErrors
import io.reactivex.Notification
import io.reactivex.disposables.CompositeDisposable

open class BaseController : Service() {

    open fun fetchData() {
        if (disposable.isDisposed)
            disposable = CompositeDisposable()
    }

    open fun destroy() {
        disposable.dispose()
    }

    protected fun handleException(error: Throwable) {
        if (error is FuelError) {
            val sportsBookException = ServerException(error.response)
            streamErrors.onNext(Notification.createOnNext(sportsBookException))
        }
    }



}