package coo.apps.weather.viemodels

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import coo.apps.weather.R
import coo.apps.weather.network.ApiPath
import coo.apps.weather.network.NetworkService
import coo.apps.weather.network.controller.MainController
import org.koin.java.KoinJavaComponent.inject


class MainViewModel(application: Application) : AndroidViewModel(application) {

    var locationCoordinatesLiveData: MutableLiveData<Location> = MutableLiveData()
    var currentLocation: Location? = null
    private val mainController: MainController by lazy { MainController() }

    fun handleNavigation(navController: NavController, navView: BottomNavigationView) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.label.toString()) {
                "Home" -> {
                    navView.menu.findItem(R.id.navigation_home).setIcon(R.drawable.ic_home_active)
                    navView.menu.findItem(R.id.navigation_maps).setIcon(R.drawable.ic_location_inactive)
                    navView.menu.findItem(R.id.navigation_charts).setIcon(R.drawable.ic_charts_inactive)


                }
                "Maps" -> {
                    navView.menu.findItem(R.id.navigation_home).setIcon(R.drawable.ic_home_inactive)
                    navView.menu.findItem(R.id.navigation_maps).setIcon(R.drawable.ic_location_active)
                    navView.menu.findItem(R.id.navigation_charts).setIcon(R.drawable.ic_charts_inactive)


                }
                "Charts" -> {
                    navView.menu.findItem(R.id.navigation_home).setIcon(R.drawable.ic_home_inactive)
                    navView.menu.findItem(R.id.navigation_charts).setIcon(R.drawable.ic_charts_active)
                    navView.menu.findItem(R.id.navigation_maps).setIcon(R.drawable.ic_location_inactive)
                }
            }
        }
    }

    fun observeCoordinates(owner: LifecycleOwner, observer: Observer<Location?>) {
        locationCoordinatesLiveData.observe(owner, observer)
    }

    fun postCoordinates(location: Location?) {
        currentLocation = location
        locationCoordinatesLiveData.postValue(location)
    }


    fun makeMainRequest() {
      val response  =  mainController.makeMainRequest(currentLocation)
        response
    }


}