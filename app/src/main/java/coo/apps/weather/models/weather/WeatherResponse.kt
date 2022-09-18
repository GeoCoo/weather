package coo.apps.weather.models.weather

data class WeatherResponse(
    var hours: Int? = null,
    var temp: ArrayList<ArrayList<Int>> = arrayListOf(),
    var apcp: ArrayList<ArrayList<Int>> = arrayListOf(),
    var wind: ArrayList<Wind> = arrayListOf(),
    var relhum: ArrayList<ArrayList<Int>> = arrayListOf(),
    var slp: ArrayList<ArrayList<Int>> = arrayListOf(),
    var dust: ArrayList<ArrayList<Int>> = arrayListOf(),
    var visib: ArrayList<ArrayList<Int>> = arrayListOf()

)


