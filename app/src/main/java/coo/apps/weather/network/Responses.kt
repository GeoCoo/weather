package coo.apps.weather.network

data class LimitsResponse(
    @JvmField var xmin: Double,
    @JvmField var xmax: Double,
    @JvmField var ymin: Double,
    @JvmField var ymax: Double
)