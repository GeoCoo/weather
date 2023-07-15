package coo.apps.meteoray.viemodels

import android.app.Application
import android.location.Address
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import coo.apps.meteoray.R
import coo.apps.meteoray.locationsDb.LocationEntity
import coo.apps.meteoray.locationsDb.LocationsRepository
import coo.apps.meteoray.models.Limits
import coo.apps.meteoray.models.Notification
import coo.apps.meteoray.models.main.MainResponse
import coo.apps.meteoray.network.controller.LimitController
import coo.apps.meteoray.network.controller.MainController
import coo.apps.meteoray.utils.getPlaceNameFromLocation


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var locationCoordinatesLiveData: MutableLiveData<Location?> = MutableLiveData()
    private var boundsMutable: MutableLiveData<Limits> = MutableLiveData()
    private var responseMutable: MutableLiveData<MainResponse?> = MutableLiveData()
    private var mapSearchMutable: MutableLiveData<Notification> = MutableLiveData()
    private val viewPagerPositionMutable: MutableLiveData<Int> = MutableLiveData(0)
    private var currentLocation: Location? = null
    private val mainController: MainController by lazy { MainController() }
    private val limitController: LimitController by lazy { LimitController() }
    private val networkStatusMutable: MutableLiveData<Boolean> = MutableLiveData()


    fun observeSearchNotification(owner: LifecycleOwner, observer: Observer<Notification>) {
        mapSearchMutable.observe(owner, observer)
    }

    fun postSearchNotification(notification: Notification) {
        mapSearchMutable.postValue(notification)
    }

    fun observeCoordinates(owner: LifecycleOwner, observer: Observer<Location?>) {
        locationCoordinatesLiveData.observe(owner, observer)
    }

    fun postCoordinates(location: Location?) {
        currentLocation = location
        locationCoordinatesLiveData.postValue(location)
    }

    suspend fun makeMainRequest(location: Location?, language: String): MainResponse? =
        mainController.makeMainRequest(location, language)

    fun postMainResponse(response: MainResponse?) {
        responseMutable.postValue(response)
    }

    fun observeMainResponse(viewLifecycleOwner: LifecycleOwner, observer: Observer<MainResponse?>) {
        responseMutable.observe(viewLifecycleOwner, observer)
    }

    fun getPlaceName(): String {
        val place: Address? = currentLocation?.getPlaceNameFromLocation(getApplication())
        return if (place?.featureName == getApplication<Application>().resources.getString(R.string.places_address)) {
            "${place.locality ?: place.subLocality ?: ""},${place.postalCode},${place.countryName} "
        } else {
            "${place?.getAddressLine(0)} "
        }
    }

    suspend fun postLimits() {
        boundsMutable.postValue(limitController.makeLimitRequest())
    }

    fun observeBounds(lifecycleOwner: LifecycleOwner, observer: Observer<Limits>) {
        boundsMutable.observe(lifecycleOwner, observer)
    }

    fun getSingleLocation(locationRepository: LocationsRepository, id: Int) =
        locationRepository.getSingleLocation(id)

    fun deleteLocation(locationRepository: LocationsRepository, locationEntity: LocationEntity) =
        locationRepository.deleteLocation(locationEntity)


    fun postPagerPosition(position: Int) {
        viewPagerPositionMutable.postValue(position)
    }

    fun observePagerPosition(viewLifecycleOwner: LifecycleOwner, observer: Observer<Int>) {
        viewPagerPositionMutable.observe(viewLifecycleOwner, observer)
    }

    fun postNetAccess(isConnected: Boolean) {
        networkStatusMutable.postValue(isConnected)
    }

    fun observeNetAccess(viewLifecycleOwner: LifecycleOwner, observer: Observer<Boolean>) {
        networkStatusMutable.observe(viewLifecycleOwner, observer)
    }
}