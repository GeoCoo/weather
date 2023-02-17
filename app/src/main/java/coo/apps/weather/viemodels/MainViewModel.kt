package coo.apps.weather.viemodels

import android.app.Application
import android.location.Address
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import coo.apps.weather.models.Limits
import coo.apps.weather.models.main.MainResponse
import coo.apps.weather.network.controller.LimitController
import coo.apps.weather.network.controller.MainController
import coo.apps.weather.utils.getPlaceNameFromLocation
import coo.apps.weather.utils.isInBoundBox


class MainViewModel(private var application: Application) : AndroidViewModel(application) {

    private var locationCoordinatesLiveData: MutableLiveData<Location?> = MutableLiveData()
    var boundsMutable: MutableLiveData<Limits> = MutableLiveData()
    private var responseMutable: MutableLiveData<MainResponse?> = MutableLiveData()
    private var bottomSheetStateMutable: MutableLiveData<Int> = MutableLiveData()

    private var currentLocation: Location? = null
    private val mainController: MainController by lazy { MainController() }
    private val limitController: LimitController by lazy { LimitController() }


    fun observeBottomSheetState(owner: LifecycleOwner, observer: Observer<Int>) {
        bottomSheetStateMutable.observe(owner, observer)
    }

    fun postBottomSheetState(state: Int) {
        bottomSheetStateMutable.postValue(state)
    }


    fun observeCoordinates(owner: LifecycleOwner, observer: Observer<Location?>) {
        locationCoordinatesLiveData.observe(owner, observer)
    }

    fun postCoordinates(location: Location?) {
        currentLocation = location
        locationCoordinatesLiveData.postValue(location)
    }

    suspend fun makeMainRequest(location: Location?): MainResponse? =
        mainController.makeMainRequest(location)

    fun postMainResponse(response: MainResponse?) {
        responseMutable.postValue(response)
    }

    fun observeMainResponse(viewLifecycleOwner: LifecycleOwner, observer: Observer<MainResponse?>) {
        responseMutable.observe(viewLifecycleOwner, observer)
    }

    fun getPlaceName(): String? {
        val place: Address? = getPlaceNameFromLocation(
            getApplication(),
            currentLocation?.latitude,
            currentLocation?.longitude
        )
        return if (place?.locality == null) place?.countryName else "${place.locality}, ${place.countryName}" ?:""
    }

    fun checkIfIsInBox(latLng: LatLng): Boolean {
        val location = Location(LocationManager.GPS_PROVIDER)
        location.latitude = latLng.latitude
        location.longitude = latLng.longitude
        return location.isInBoundBox(boundsMutable.value!!)
    }

    suspend fun getLimits() {
        boundsMutable.postValue(limitController.makeLimitRequest())
    }

    fun observeBounds(lifecycleOwner: LifecycleOwner, observer: Observer<Limits>) {
        boundsMutable.observe(lifecycleOwner, observer)
    }


}