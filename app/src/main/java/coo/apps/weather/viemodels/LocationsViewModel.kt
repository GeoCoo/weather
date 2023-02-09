package coo.apps.weather.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import coo.apps.weather.models.LocationsDb.LocationRoom

class LocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val locationsMutable :MutableLiveData<List<LocationRoom?>> = MutableLiveData()

    private fun postLocations(locations:List<LocationRoom?>){
        locationsMutable.postValue(locations)
    }

    fun observeLocations(owner: LifecycleOwner, observer: Observer<List<LocationRoom?>>){
        locationsMutable.observe(owner,observer)
    }


}