package coo.apps.meteoray.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import coo.apps.meteoray.locationsDb.LocationEntity
import coo.apps.meteoray.models.NavigationDest

class NavigationViewModel(application: Application) : AndroidViewModel(application) {

    private var destinationNavMutable: MutableLiveData<Int> = MutableLiveData()
    private var navigationDesMutable: MutableLiveData<Pair<NavigationDest?, LocationEntity?>?> =
        MutableLiveData()


    fun postDestinationNav(destination: Int) {
        destinationNavMutable.postValue(destination)
    }

    fun observerDestinationNav(lifecycleOwner: LifecycleOwner, observer: Observer<Int>) {
        destinationNavMutable.observe(lifecycleOwner, observer)
    }

    fun postNavigation(item: Pair<NavigationDest?, LocationEntity?>) {
        navigationDesMutable.postValue(item)
    }

    fun observeNavigation(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<Pair<NavigationDest?, LocationEntity?>?>
    ) {
        navigationDesMutable.observe(lifecycleOwner, observer)
    }
}