package coo.apps.weather.viemodels

import android.app.Application
import android.location.Location
import androidx.lifecycle.*
import coo.apps.weather.models.weather.WeatherResponse
import coo.apps.weather.network.controller.HighChartOceanController
import coo.apps.weather.network.controller.HighChartWaveController
import coo.apps.weather.network.controller.HighChartsWeatherController
import kotlinx.coroutines.launch

class ChartsViewModel(application: Application) : AndroidViewModel(application) {

    private val highChartOceanController: HighChartOceanController by lazy { HighChartOceanController() }
    private val highChartsWeatherController: HighChartsWeatherController by lazy { HighChartsWeatherController() }
    private val highChartWaveController: HighChartWaveController by lazy { HighChartWaveController() }
    private val weatherMutable: MutableLiveData<WeatherResponse?> = MutableLiveData()


    suspend fun makeOceanRequest(location: Location?) = highChartOceanController.makeOceanRequest(location)

    suspend fun makeWeatherRequest(location: Location?) = highChartsWeatherController.makeWeatherRequest(location)

    suspend fun makeWaveRequest(location: Location?) = highChartWaveController.makeWaveRequest(location)


    private fun postWeatherResponse(weatherResponse: WeatherResponse?) {
        weatherMutable.postValue(weatherResponse)
    }

    fun observeWeatherResponse(lifecycleOwner: LifecycleOwner, observer: Observer<WeatherResponse?>) {
        weatherMutable.observe(lifecycleOwner, observer)
    }

    fun handleWeather(location: Location?) {
        viewModelScope.launch {
            val response = makeWeatherRequest(location)
            postWeatherResponse(response)
        }
    }

}