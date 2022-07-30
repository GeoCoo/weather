package coo.apps.weather.models.main

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("desc") var desc: String? = null,
    @SerializedName("bgclass") var bgclass: String? = null,
    @SerializedName("icon") var icon: String? = null,
    @SerializedName("temp") var temp: String? = null,
    @SerializedName("dust") var dust: String? = null,
    @SerializedName("precip") var precip: String? = null,
    @SerializedName("relhum") var relhum: String? = null,
    @SerializedName("vis") var vis: String? = null,
    @SerializedName("wind10") var wind10: String? = null,
    @SerializedName("wind10dir") var wind10dir: String? = null

)