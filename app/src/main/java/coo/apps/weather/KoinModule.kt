package coo.apps.weather

import coo.apps.weather.viemodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class KoinModule(application: MeteoRayApplication) {
    val modules = module {
        viewModel { MainViewModel(application) }
    }
}