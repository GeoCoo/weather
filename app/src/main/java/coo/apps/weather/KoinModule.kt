package coo.apps.weather

import coo.apps.weather.locationsDb.LocationsRepository
import coo.apps.weather.viemodels.DatabaseViewModel
import coo.apps.weather.viemodels.MainViewModel
import coo.apps.weather.viemodels.NavigationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class KoinModule(application: MeteoRayApplication) {
    val modules = module {
        viewModel { MainViewModel(application) }
        viewModel { DatabaseViewModel(application) }
        viewModel { NavigationViewModel(application) }
        single { LocationsRepository(application) }
    }
}