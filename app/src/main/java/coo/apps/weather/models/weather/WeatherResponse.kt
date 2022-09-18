package coo.apps.weather.models.weather

data class WeatherResponse(
    val apcp: List<List<Long>>,
    val dust: List<List<Double>>,
    val hours: Int,
    val relhum: List<List<Double>>,
    val slp: List<List<Double>>,
    val temp: List<List<Double>>,
    val visib: List<List<Double>>,
    val wind: List<Wind>

)


