package coo.apps.weather.network

import android.location.Location
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication

enum class ApiPath {
    main,
    wave,
    ocean,
    weather,
    boundingBox

}


class NetworkService() {

    private fun getRelativePath(path: ApiPath): String {
        return when (path) {
            ApiPath.main -> {
                "/frontPage_v3.php/"
            }
            ApiPath.weather -> {
                "/highcharts_weather_json.php/"
            }
            ApiPath.ocean -> {
                "/highcharts_ocean_json.php/"
            }
            ApiPath.wave -> {
                "/highcharts_wave_json.php/"
            }
            ApiPath.boundingBox -> {
                "limits.php/"
            }
        }
    }
}

