package coo.apps.meteoray

import coo.apps.meteoray.locationsDb.LocationsRepository
import coo.apps.meteoray.viemodels.DatabaseViewModel
import coo.apps.meteoray.viemodels.MainViewModel
import coo.apps.meteoray.viemodels.NavigationViewModel
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