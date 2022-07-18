package coo.apps.weather.network.request

import android.location.Location
import coo.apps.weather.network.ApiPath

class MainRequest( location: Location?) : BaseRequest() {

    override var method: Method
        get() = Method.GET
        set(value) {}


    override var path: String = "limits.php/latlon:${location?.latitude},${location?.longitude}"

}