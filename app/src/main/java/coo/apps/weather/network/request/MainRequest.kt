package coo.apps.weather.network.request

import android.location.Location
import com.github.kittinunf.fuel.core.Parameters

class MainRequest(location: Location?, lan: String = "el") : BaseRequest() {

    override var method: Method
        get() = Method.GET
        set(value) {}

    override var path: String = "frontpage.php"
    override var queryParameter: Parameters? =
        listOf(Pair("lat", location?.latitude), Pair("lon", location?.longitude), Pair("lan", lan))


}

