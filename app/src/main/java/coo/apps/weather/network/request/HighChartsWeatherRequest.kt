package coo.apps.weather.network.request

import android.location.Location
import com.github.kittinunf.fuel.core.Parameters

class HighChartsWeatherRequest(location: Location?) : BaseRequest() {
    override var method: Method
        get() = Method.GET
        set(value) {}

    override var path: String = "highcharts_weather_json.php"

    override var queryParameter: Parameters? = listOf(Pair("latlon", "${location?.latitude},${location?.longitude}"))


}