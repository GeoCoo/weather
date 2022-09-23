package coo.apps.weather.viemodels

import android.app.Application
import android.location.Address
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
import coo.apps.weather.utils.getPlaceNameFromLocation
import coo.apps.weather.utils.handleBoundBox


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var locationCoordinatesLiveData: MutableLiveData<Location?> = MutableLiveData()
    var boundsMutable: MutableLiveData<Limits> = MutableLiveData()
    private var responseMutable: MutableLiveData<MainResponse?> = MutableLiveData()

    private var currentLocation: Location? = null
    private val mainController: MainController by lazy { MainController() }
    private val limitController: LimitController by lazy { LimitController() }


    //TODO:TEST ONLY
//    private var currentLocation: Location? = Location("33.8932174,35.4803467")


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

    suspend fun makeMainRequest(location: Location?): MainResponse? = mainController.makeMainRequest(location)

    fun postMainResponse(response: MainResponse?) {
        responseMutable.postValue(response)
    }

    fun observeMainResponse(viewLifecycleOwner: LifecycleOwner, observer: Observer<MainResponse?>) {
        responseMutable.observe(viewLifecycleOwner, observer)
    }

    fun getPlaceName(): String? {
        var place: Address? = null
        return if (currentLocation?.handleBoundBox(boundsMutable.value!!) == true) {
            place = getPlaceNameFromLocation(getApplication(), currentLocation?.latitude, currentLocation?.longitude)
            if (place?.locality == null) place?.countryName else "${place.locality}, ${place.countryName}"
        } else
            "The selection is outside of the supported area"
    }


    suspend fun getLimits() {
        boundsMutable.postValue(limitController.makeLimitRequest())
    }

    fun observeBounds(lifecycleOwner: LifecycleOwner, observer: Observer<Limits>) {
        boundsMutable.observe(lifecycleOwner, observer)
    }


}