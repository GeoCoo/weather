package coo.apps.weather.models.weather


data class WeatherResponse(
    val apcp: ArrayList<List<Double>>,
    val dust: ArrayList<List<Double>>,
    val hours: Int,
    val relhum: ArrayList<List<Double>>,
    val slp: ArrayList<List<Double>>,
    val temp: ArrayList<List<Double>>,
    val visib: ArrayList<List<Double>>,
    val wind: ArrayList<Wind>
)


data class AcppModel(
    @JvmField val apcp: ArrayList<List<Double>>
)

data class DustModel(
    @JvmField val dust: ArrayList<List<Double>>
)

data class RehlumModel(
    @JvmField val rehlum: ArrayList<List<Double>>
)

data class SlpModel(
    @JvmField val slp: ArrayList<List<Double>>
)

data class TempModel(
    @JvmField val temp: ArrayList<List<Double>>
)

data class VisibModel(
    @JvmField val visib: ArrayList<List<Double>>
)

data class WindModel(
    @JvmField val wind: ArrayList<Wind>
)


