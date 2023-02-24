package coo.apps.meteoray.base

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coo.apps.meteoray.activities.MainActivity
import coo.apps.meteoray.locationsDb.LocationsRepository
import coo.apps.meteoray.viemodels.DatabaseViewModel
import coo.apps.meteoray.viemodels.MainViewModel
import coo.apps.meteoray.viemodels.NavigationViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

open class BaseActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private val locationRequestCode = 0x123
    protected val locationRepository: LocationsRepository by inject()

    protected lateinit var phoneLanguage: String

    val mainViewModel: MainViewModel by viewModel()
    val databaseViewModel: DatabaseViewModel by viewModel()
    val navigation: NavigationViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        phoneLanguage = getLanguage()
        lifecycleScope.launch {
            mainViewModel.getLimits()
        }
        if (this is MainActivity) {
            val locationPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (locationPermission == PackageManager.PERMISSION_GRANTED) {
                updateUserLocation()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationRequestCode
                )
            }


        }
    }


    override fun onLocationChanged(location: Location) {
        mainViewModel.postCoordinates(location)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun handleLocation() {
        if (this is MainActivity) {
            val locationPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (locationPermission == PackageManager.PERMISSION_GRANTED) {
                updateUserLocation()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationRequestCode
                )
            }
        }
    }

    private fun updateUserLocation() {
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        mainViewModel.postCoordinates(location)
    }


    private fun getLanguage(): String {
        return Locale.getDefault().language
    }


}