package coo.apps.meteoray.viemodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import coo.apps.meteoray.R
import coo.apps.meteoray.models.NavigationDest

class NavigationViewModel(application: Application) : AndroidViewModel(application) {

    fun handleNavigation(navView: NavHostFragment?, destination: NavigationDest) {
        when (destination) {
            NavigationDest.HOME -> {
                navView?.findNavController()?.navigate(R.id.navigation_home)
            }
            NavigationDest.MAPS -> {
                navView?.findNavController()?.navigate(R.id.navigation_maps)
            }
            NavigationDest.ACTIONS -> {
                navView?.findNavController()?.navigate(R.id.navigation_actions)
            }
            NavigationDest.LOCATIONS -> {
                navView?.findNavController()?.navigate(R.id.navigation_locations)
            }
            NavigationDest.SETTINGS -> {
                navView?.findNavController()?.navigate(R.id.navigation_settings)
            }
        }
    }
}