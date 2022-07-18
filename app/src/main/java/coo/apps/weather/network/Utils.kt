package coo.apps.weather.network

import com.github.kittinunf.fuel.core.Response
import io.reactivex.Notification
import io.reactivex.subjects.PublishSubject

val streamErrors: PublishSubject<Notification<Exception>> = PublishSubject.create()

data class ServerException(val code: Int, val url: String?, val reason: String?) : Exception(reason) {
    constructor(response: Response) : this(response.statusCode, response.url.toExternalForm(), response.responseMessage)
    constructor(method: String, exception: Throwable?) : this(9000, method, exception?.message)
    constructor(method: String, reason: String?) : this(9000, method, reason)
}


data class WassfException(val code: Int, override val message: String?, val method: String? = null, override val cause: Throwable? = null, val description: String? = null) : Exception(message, cause)
