package coo.apps.meteoray.models.main

import com.google.gson.annotations.SerializedName

data class DayTable(
    @SerializedName("dir") var dir: Int? = null,
    @SerializedName("dirname") var dirname: String? = null,
    @SerializedName("icon") var icon: String = "",
    @SerializedName("temp") var temp: String? = null,
    @SerializedName("time") var time: String? = null,
    @SerializedName("wind") var wind: String? = null,
    @SerializedName("windbft") var windbft: String? = null
)


