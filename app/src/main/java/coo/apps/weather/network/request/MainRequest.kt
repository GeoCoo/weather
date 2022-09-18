package coo.apps.weather.network.request

import android.location.Location
import com.github.kittinunf.fuel.core.Parameters

class MainRequest(location: Location?) : BaseRequest() {

    override var method: Method
        get() = Method.GET
        set(value) {}

    override var path: String = "frontpage_v3.php"
//    override var queryParameter: Parameters? = listOf(Pair("latlon", "${location?.latitude},${location?.longitude}"))
//override var queryParameter: Parameters? = listOf(Pair("latlon", "29.3117, 47.4818"))
    override var queryParameter: Parameters? = listOf(Pair("latlon","${location?.latitude},${location?.longitude}"))




}

