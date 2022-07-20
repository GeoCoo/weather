package coo.apps.weather.network.request

import android.location.Location

class HighChartsWaveRequest(location: Location?) : BaseRequest() {
    override var method: Method
        get() = Method.GET
        set(value) {}

    override var path: String = "highcharts_wave_json.php/latlon:${location?.latitude},${location?.longitude}"
}