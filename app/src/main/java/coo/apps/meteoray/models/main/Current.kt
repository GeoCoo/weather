package coo.apps.meteoray.models.main

import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("bgclass") var bgclass: String = "",
    @SerializedName("desc") var desc: String? = null,
    @SerializedName("icon") var icon: String = "",
    @SerializedName("precip") var precip: String? = null,
    @SerializedName("relhum") var relhum: String? = null,
    @SerializedName("temp") var temp: String? = null,
    @SerializedName("tempfrt") var tempfrt: String? = "",
    @SerializedName("wind10") val wind10: String? = "",
    @SerializedName("wind10bft") var wind10bft: String? = null,
    @SerializedName("wind10dir") var wind10dir: String? = null,
    @SerializedName("down") val down: String,
    @SerializedName("hail") val hail: String,
    @SerializedName("up") val up: String,

    )