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
import coo.apps.weather.models.Limits
import coo.apps.weather.models.main.MainResponse
import coo.apps.weather.network.controller.LimitController
import coo.apps.weather.network.controller.MainController
import coo.apps.weather.network.getPlaceNameFromLocation


class MainViewModel(application: Application) : AndroidViewModel(application) {


    var locationCoordinatesLiveData: MutableLiveData<Location?> = MutableLiveData()
    var boundsMutable: MutableLiveData<Limits> = MutableLiveData()
    var currentLocation: Location? = null
    private val mainController: MainController by lazy { MainController() }
    private val limitController: LimitController by lazy { LimitController() }

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

    suspend fun makeMainRequest(): MainResponse? = mainController.makeMainRequest(currentLocation)

    fun getPlaceName(): String {
//        return getPlaceNameFromLocation(getApplication<Application>().applicationContext,currentLocation?.latitude, currentLocation?.longitude)
        val place = getPlaceNameFromLocation(getApplication(), 33.8932174, 35.4803467)
        return place?.locality + "," + place?.countryName

    }


   suspend fun getLimits() {
       boundsMutable.postValue(limitController.makeLimitRequest())
    }

    fun observeBounds(lifecycleOwner: LifecycleOwner,observer: Observer<Limits>){
        boundsMutable.observe(lifecycleOwner,observer)

    }


}