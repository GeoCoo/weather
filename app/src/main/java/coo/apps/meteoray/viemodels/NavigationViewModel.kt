package coo.apps.meteoray.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import coo.apps.meteoray.models.NavigationDest

class NavigationViewModel(application: Application) : AndroidViewModel(application) {

    private var destinationNavMutable: MutableLiveData<Int> = MutableLiveData()
    private var navigationDesMutable: MutableLiveData<NavigationDest?> = MutableLiveData()


    fun postDestinationNav(destination: Int) {
        destinationNavMutable.postValue(destination)
    }

    fun observerDestinationNav(lifecycleOwner: LifecycleOwner, observer: Observer<Int>) {
        destinationNavMutable.observe(lifecycleOwner, observer)
    }

    fun postNavigation(destination: NavigationDest?) {
        navigationDesMutable.postValue(destination)
    }

    fun observeNavigation(lifecycleOwner: LifecycleOwner, observer: Observer<NavigationDest?>) {
        navigationDesMutable.observe(lifecycleOwner, observer)
    }
}