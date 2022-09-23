package coo.apps.weather.models.weather

data class WeatherResponse(
    val apcp: AcppModel,
    val dust: DustModel,
    val hours: Int,
    val relhum: RehlumModel,
    val slp: SlpModel,
    val temp: TempModel,
    val visib: VisibModel,
    val wind: WindModel
)


data class AcppModel(
    @JvmField val apcp: ArrayList<List<Long>>
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


