package coo.apps.weather.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import coo.apps.weather.locationsDb.Location
import coo.apps.weather.locationsDb.LocationRoom

class LocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val locationListsMutable: MutableLiveData<List<LocationRoom?>> = MutableLiveData()
    private val locationMutable: MutableLiveData<Location> = MutableLiveData()

    fun postLocations(locations: List<LocationRoom?>) {
        locationListsMutable.postValue(locations)
    }

    fun observeLocations(owner: LifecycleOwner, observer: Observer<List<LocationRoom?>>) {
        locationListsMutable.observe(owner, observer)
    }

    fun postSingleLocation(location: Location) {
        locationMutable.postValue(location)
    }

    fun observeSingleLocation(owner: LifecycleOwner, observer: Observer<Location>) {
        locationMutable.observe(owner, observer)
    }

    fun addNewLocation(locationRoom: LocationRoom) {

    }


}