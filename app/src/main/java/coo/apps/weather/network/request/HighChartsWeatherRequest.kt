package coo.apps.weather.network.request

import android.location.Location

class HighChartsWeatherRequest(location: Location?) : BaseRequest() {
    override var method: Method
        get() = Method.GET
        set(value) {}

    override var path: String = "highcharts_weather_json.php/latlon:${location?.latitude},${location?.longitude}"
}