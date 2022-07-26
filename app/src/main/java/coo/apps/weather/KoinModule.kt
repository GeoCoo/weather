package coo.apps.weather

import coo.apps.weather.viemodels.ChartsViewModel
import coo.apps.weather.viemodels.HomeViewModel
import coo.apps.weather.viemodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class KoinModule(application: WeatherApplication) {
    val modules = module {
        viewModel { MainViewModel(application) }
        viewModel { HomeViewModel(application) }
        viewModel { ChartsViewModel(application) }
    }
}