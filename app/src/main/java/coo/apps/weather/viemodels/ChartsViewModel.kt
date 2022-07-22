package coo.apps.weather.viemodels

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import coo.apps.weather.network.controller.HighChartOceanController
import coo.apps.weather.network.controller.HighChartWaveController
import coo.apps.weather.network.controller.HighChartsWeatherController

class ChartsViewModel(application: Application) : AndroidViewModel(application) {

    private val highChartOceanController: HighChartOceanController by lazy { HighChartOceanController() }
    private val highChartsWeatherController: HighChartsWeatherController by lazy { HighChartsWeatherController() }
    private val highChartWaveController: HighChartWaveController by lazy { HighChartWaveController() }


    fun makeOceanRequest(location: Location?) {
        val response = highChartOceanController.makeOceanRequest(location)
        response
    }

    fun makeWeatherRequest(location: Location?) {
        val response = highChartsWeatherController.makeWeatherRequest(location)
        response
    }

    fun makeWaveRequest(location: Location?) {
        val response = highChartWaveController.makeWaveRequest(location)
        response
    }

}