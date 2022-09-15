package coo.apps.weather.models

data class Limits(
    @JvmField var xmin: Double,
    @JvmField var xmax: Double,
    @JvmField var ymin: Double,
    @JvmField var ymax: Double
)