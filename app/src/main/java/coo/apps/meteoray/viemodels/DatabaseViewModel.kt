package coo.apps.meteoray.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import coo.apps.meteoray.locationsDb.LocationEntity
import coo.apps.meteoray.locationsDb.LocationsRepository
import coo.apps.meteoray.models.DbAction

class DatabaseViewModel(application: Application) : AndroidViewModel(application) {

    private val locationListsMutable: MutableLiveData<List<LocationEntity?>> = MutableLiveData()
    private val locationMutable: MutableLiveData<LocationEntity> = MutableLiveData()
    private val dbActionsMutable: MutableLiveData<Pair<DbAction, LocationEntity>> =
        MutableLiveData()

    fun postDbAction(item: Pair<DbAction, LocationEntity>) {
        dbActionsMutable.postValue(item)
    }

    fun observeDbAction(owner: LifecycleOwner, observer: Observer<Pair<DbAction, LocationEntity>>) {
        dbActionsMutable.observe(owner, observer)
    }

    fun postLocations(locations: List<LocationEntity?>) {
        locationListsMutable.postValue(locations)
    }

    fun observeLocations(owner: LifecycleOwner, observer: Observer<List<LocationEntity?>>) {
        locationListsMutable.observe(owner, observer)
    }

    fun postSingleLocation(location: LocationEntity) {
        locationMutable.postValue(location)
    }

    fun observeSingleLocation(owner: LifecycleOwner, observer: Observer<LocationEntity>) {
        locationMutable.observe(owner, observer)
    }

    fun addNewLocation(locationEntity: LocationEntity) {
        locationMutable.postValue(locationEntity)
    }


    fun handleLocationActions(
        action: DbAction,
        locationRepository: LocationsRepository,
        location: LocationEntity?
    ) {
        when (action) {
            DbAction.SAVE -> {
                locationRepository.insertNewLocation(location)
            }

            DbAction.EDIT -> {
                locationRepository.updateLocation(location)
            }
        }
    }
}