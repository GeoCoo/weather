package coo.apps.meteoray.models.main

import com.google.gson.annotations.SerializedName
import coo.apps.meteoray.R
import coo.apps.meteoray.models.Icon

data class MainResponse(
    @SerializedName("current") var current: Current,
    @SerializedName("overview") var overview: List<Overview>,
    @SerializedName("day_table") var dayTable: List<DayTable>

)


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
        Icon.cloudy.type -> R.drawable.ic_cloudy_bg
        Icon.dusty.type -> R.drawable.ic_dusty_bg
        Icon.foggy.type -> R.drawable.ic_fog_bg
        Icon.heavyClouds.type -> R.drawable.ic_heavy_clouds_bg
        Icon.heavyRain.type -> R.drawable.ic_heavy_rain_bg
        Icon.nighhtClear.type -> R.drawable.ic_night_clear
        Icon.nightClouds.type -> R.drawable.ic_night_bg_cloud
        Icon.rainy.type -> R.drawable.ic_rainy_bg
        Icon.snowy.type -> R.drawable.ic_snow_bg
        Icon.sunny.type -> R.drawable.ic_sunny_bg
        else -> R.drawable.ic_night_bg_cloud
    }
}

