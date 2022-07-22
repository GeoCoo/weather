package coo.apps.weather.viemodels

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import coo.apps.weather.network.controller.MainController

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val mainController: MainController by lazy { MainController() }

    fun makeMainRequest(location: Location?) {
        val response = mainController.makeMainRequest(location)
        response
    }


}