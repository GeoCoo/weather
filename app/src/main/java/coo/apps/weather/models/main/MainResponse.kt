package coo.apps.weather.models.main

import com.google.gson.annotations.SerializedName
import coo.apps.weather.R

data class MainResponse(
    @SerializedName("current") var current: Current? = Current(),
    @SerializedName("overview") var overview: ArrayList<Overview> = arrayListOf(),
    @SerializedName("day_table") var dayTable: ArrayList<DayTable> = arrayListOf()

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
    fog("weather-icon-fog.svg"),
    nightClouds("night-clouds")

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


fun getSmallIcons(icon: String): Int {
    return when (icon) {
        Icon.clouds.type -> R.drawable.ic_icon_heavy_clouds_small
        Icon.fog.type -> R.drawable.ic_icon_fog_small
        Icon.hail.type -> R.drawable.ic_icon_hail_small
        Icon.moon.type -> R.drawable.ic_icon_moon_small
        Icon.dust.type -> R.drawable.ic_icon_dust_small
        Icon.moonCloud.type -> R.drawable.ic_icon_moon_cloud_small
        Icon.snow.type -> R.drawable.ic_icon_snow_sma
        Icon.sun.type -> R.drawable.ic_icon_sunny_small
        Icon.sunCloud.type -> R.drawable.ic_icon_sun_clouds_small
        Icon.rain.type -> R.drawable.ic_icon_rain_small
        Icon.thunderstorm.type -> R.drawable.ic_icon_thunderstorm_small
        else -> R.drawable.ic_icon_sunny_small
    }
}

fun getBg(icon: String): Int {
    return when (icon) {
        Icon.nightClouds.type -> R.drawable.ic_night_bg_cloud

        else -> {R.drawable.ic_night_bg_cloud}
    }
}

