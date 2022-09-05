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


