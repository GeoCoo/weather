package coo.apps.weather.network

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.github.kittinunf.fuel.core.Response
import io.reactivex.Notification
import io.reactivex.subjects.PublishSubject
import java.text.SimpleDateFormat

val streamErrors: PublishSubject<Notification<Exception>> = PublishSubject.create()

data class ServerException(val code: Int, val url: String?, val reason: String?) : Exception(reason) {
    constructor(response: Response) : this(response.statusCode, response.url.toExternalForm(), response.responseMessage)
    constructor(method: String, exception: Throwable?) : this(9000, method, exception?.message)
    constructor(method: String, reason: String?) : this(9000, method, reason)
}


data class WassfException(val code: Int, override val message: String?, val method: String? = null, override val cause: Throwable? = null, val description: String? = null) : Exception(message, cause)


fun getPlaceNameFromLocation(context: Context, lat: Double?, long: Double?): Address? {
    val geocoder = Geocoder(context)
    val addresses = geocoder.getFromLocation(lat!!, long!!, 1)
    return addresses[0]
}

fun convertDate(date:String?): String{
    val formatter = SimpleDateFormat("MM/dd/yyyy")
    val finalDate = formatter.parse(date.toString())
    return SimpleDateFormat("EEE d").format(finalDate!!)





}