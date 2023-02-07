package coo.apps.weather.models.main

import com.google.gson.annotations.SerializedName

data class Overview(
    @SerializedName("date") var date: String? = null,
    @SerializedName("desc") var desc: String? = null,
    @SerializedName("icon") var icon: String = "",
    @SerializedName("name") var name: String? = null,
    @SerializedName("precipmax") var precipmax: String? = null,
    @SerializedName("precipmin") var precipmin: String? = null,
    @SerializedName("relhummax") var relhummax: String? = null,
    @SerializedName("relhummin") var relhummin: String? = null,
    @SerializedName("tempmax") var tempmax: String? = null,
    @SerializedName("tempmin") var tempmin: String? = null,
    @SerializedName("winddir") var winddir: Int? = null,
    @SerializedName("winddirname") var winddirname: String? = null,
    @SerializedName("windspeed") var windspeed: Int? = null,
    @SerializedName("windspeedbft") var windspeedbft: String? = null
)



