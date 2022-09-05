package coo.apps.weather.models.main

import com.google.gson.annotations.SerializedName
import coo.apps.weather.R

data class DayTable(
    @SerializedName("time") var time: String? = null,
    @SerializedName("icon") var icon: String = "",
    @SerializedName("dustmax") var dustmax: String? = null,
    @SerializedName("temp") var temp: String? = null,
    @SerializedName("wind") var wind: String? = null,
    @SerializedName("dir") var dir: Int? = null,
    @SerializedName("dirname") var dirname: String? = null
)

enum class Icon(var type: String) {
    sun("weather-icon-sun.svg"),
    sunCloud("weather-icon-sun-cloud.svg"),
    clouds("weather-icon-clouds.svg"),
    rain("weather-icon-rain.svg"),
    snow("weather-icon-snow.svg"),
    thunderstorm("weather-icon-thunderstorm.svg"),
    hail("weather-icon-hail.svg"),
    moon("weather-icon-moon.svg"),
    moonCloud("weather-icon-moon-cloud.svg"),
    dust("weather-icon-dust.svg"),
    fog("weather-icon-fog.svg")
}

fun getIcon(icon: String): Int {
    return when (icon) {
        Icon.clouds.type -> R.drawable.ic_heavy_clouds
        Icon.fog.type -> R.drawable.ic_fog
        Icon.hail.type -> R.drawable.ic_hail_icon
        Icon.moon.type -> R.drawable.ic_moon_icon
        Icon.dust.type -> R.drawable.ic_dust
        Icon.moonCloud.type -> R.drawable.ic_moon_cloud_icon
        Icon.snow.type -> R.drawable.ic_snow
        Icon.sun.type -> R.drawable.ic_sunny
        Icon.sunCloud.type -> R.drawable.ic_cloudy
        Icon.rain.type -> R.drawable.ic_rain
        Icon.thunderstorm.type -> R.drawable.ic_heavy_rain
        else -> R.drawable.ic_sunny
    }
}
