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


    suspend fun makeOceanRequest(location: Location?) = highChartOceanController.makeOceanRequest(location)

    suspend fun makeWeatherRequest(location: Location?) = highChartsWeatherController.makeWeatherRequest(location)

    suspend fun makeWaveRequest(location: Location?) = highChartWaveController.makeWaveRequest(location)

}