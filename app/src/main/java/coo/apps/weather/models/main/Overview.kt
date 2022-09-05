package coo.apps.weather.models.main

import com.google.gson.annotations.SerializedName
import coo.apps.weather.R

data class Overview(
    @SerializedName("name") var name: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("icon") var icon: String = "",
    @SerializedName("desc") var desc: String? = null,
    @SerializedName("tempmax") var tempmax: String? = null,
    @SerializedName("tempmin") var tempmin: String? = null,
    @SerializedName("dustmax") var dustmax: String? = null,
    @SerializedName("precipmin") var precipmin: String? = null,
    @SerializedName("precipmax") var precipmax: String? = null,
    @SerializedName("relhummin") var relhummin: String? = null,
    @SerializedName("relhummax") var relhummax: String? = null,
    @SerializedName("windspeed") var windspeed: Int? = null,
    @SerializedName("winddir") var winddir: Int? = null,
    @SerializedName("winddirname") var winddirname: String? = null
)



