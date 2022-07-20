package coo.apps.weather.network.request

import android.location.Location
import coo.apps.weather.network.ApiPath

class MainRequest(location: Location?) : BaseRequest() {

    override var method: Method
        get() = Method.GET
        set(value) {}

    override var path: String = "frontPage_v3.php/latlon:${location?.latitude},${location?.longitude}"


}