package coo.apps.weather.models.main

import com.google.gson.annotations.SerializedName

data class MainResponse(
    @SerializedName("current") var current: Current? = Current(),
    @SerializedName("overview") var overview: ArrayList<Overview> = arrayListOf(),
    @SerializedName("day_table") var dayTable: ArrayList<DayTable> = arrayListOf()

)


