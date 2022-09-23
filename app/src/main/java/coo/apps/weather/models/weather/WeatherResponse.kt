package coo.apps.weather.models.weather

data class WeatherResponse(
    val apcp: ArrayList<List<Long>>,
    val dust: ArrayList<List<Double>>,
    val hours: Int,
    val relhum: ArrayList<List<Double>>,
    val slp: ArrayList<List<Double>>,
    val temp: ArrayList<List<Double>>,
    val visib: ArrayList<List<Double>>,
    val wind: ArrayList<Wind>

)


