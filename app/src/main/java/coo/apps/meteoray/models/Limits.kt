package coo.apps.meteoray.models

data class Limits(
    @JvmField var latmin: Double,
    @JvmField var latmax: Double,
    @JvmField var lonmin: Double,
    @JvmField var lonmax: Double
)