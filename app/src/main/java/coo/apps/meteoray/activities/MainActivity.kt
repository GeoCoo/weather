package coo.apps.meteoray.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import coo.apps.meteoray.R
import coo.apps.meteoray.base.BaseActivity
import coo.apps.meteoray.databinding.ActivityMainBinding
import coo.apps.meteoray.locationsDb.LocationEntity
import coo.apps.meteoray.managers.NetworkStatus
import coo.apps.meteoray.managers.NetworkStatusHelper
import coo.apps.meteoray.models.DbAction
import coo.apps.meteoray.models.NavigationDest
import coo.apps.meteoray.models.Notification
import coo.apps.meteoray.utils.createLocation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private lateinit var binding: ActivityMainBinding

    private lateinit var navView: NavHostFragment
    private lateinit var singleLocation: LocationEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.observeNetworkStatus(this@MainActivity) {
            when (it) {
                NetworkStatus.Available -> {
                    setFusionProvider()
                    getLocation()
                    setNaview()
                    mainInit()
                    handlePager()
                }

                NetworkStatus.Unavailable -> {
//                    showNetError()
                }
            }
        }

    }

    private fun setNaview() {
        navView =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
    }

    private fun setFusionProvider() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun handlePager() {
        mainViewModel.observePagerPosition(this) { position ->
            if (locationRepository.getAllLocations().isNotEmpty()) {
                val location = locationRepository.getAllLocations()[position]
                lifecycleScope.launch {
                    val response =
                        mainViewModel.makeMainRequest(location?.createLocation(), phoneLanguage)
                    mainViewModel.postMainResponse(response)
                }
            }
        }
    }

    private fun mainInit() {
        setStoredLocations()
        getLocationToBeStored()
        handleDbActions()
        getLocation()
        observerNavigation()
        handleCoordinates()
        handleOutOfBoundSearch()

    }

    private fun handleOutOfBoundSearch() {
        mainViewModel.observeSearchNotification(this) {
            if (it == Notification.FAIL) {
                Toast(this).setNotificationToast(
                    R.string.location_toast_error_title,
                    R.string.location_out_of_box,
                    R.color.color_danger,
                    this,
                    Toast.LENGTH_SHORT
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navView.findNavController().popBackStack()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleLocation(
        action: DbAction,
        locationEntity: LocationEntity,
        editTextInput: String
    ): LocationEntity? {
        return when (action) {
            DbAction.EDIT -> {
                LocationEntity(
                    uid = locationEntity.uid,
                    locationName = editTextInput,
                    locationLon = locationEntity.locationLon,
                    locationLat = locationEntity.locationLat
                )
            }

            DbAction.SAVE -> {
                LocationEntity(
                    locationName = editTextInput,
                    locationLon = locationEntity.locationLon,
                    locationLat = locationEntity.locationLat
                )
            }

            else -> {
                null
            }
        }
    }

    private fun getLocationToBeStored() {
        databaseViewModel.observeSingleLocation(this) {
            singleLocation = it
        }
    }


    private fun observerNavigation() {
        navigation.observeNavigation(this) { destination ->
            when (destination) {
                NavigationDest.HOME -> {
                    navView.findNavController().navigate(R.id.navigation_home)
                }

                NavigationDest.MAPS -> {
                    navView.findNavController().navigate(R.id.navigation_maps)
                }

                NavigationDest.LOCATIONS -> {
                    navView.findNavController().navigate(R.id.navigation_locations)
                }

                NavigationDest.SETTINGS -> {
                    navView.findNavController().navigate(R.id.navigation_settings)
                }

                NavigationDest.INFO -> {
                    navView.findNavController().navigate(R.id.navigation_info)
                }

                null -> {}
            }
        }
    }


    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    object : CancellationToken() {
                        override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
                            CancellationTokenSource().token

                        override fun isCancellationRequested(): Boolean = false
                    }).addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        mainViewModel.postCoordinates(location)
                    }
                }

            } else {
                setLocationNotificationDialog()
            }
        } else {
            requestPermissions()
        }
    }

    private fun setLocationNotificationDialog() {
        val dialog =
            AlertDialog.Builder(this).setTitle(resources.getString(R.string.attention_alert_dialog))
                .setMessage(resources.getString(R.string.attention_alet_dialog_text))
                .setCancelable(false)
                .setPositiveButton(resources.getString(R.string.location_ok_action)) { _, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
                .create()
        dialog.show()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            getLocation()

        }
    }

    private fun handleDbActions() {
        databaseViewModel.observeDbAction(this) { pair ->
            val (action, location) = pair
            when (action) {
                DbAction.SAVE -> {
                    handleAction(DbAction.SAVE, location)
                }

                DbAction.DELETE -> {
                    deleteLocation(location)
                    if (locationRepository.getAllLocations().isEmpty()) getLocation()
                }

                DbAction.EDIT -> {
                    val storedLocation =
                        mainViewModel.getSingleLocation(locationRepository, location.uid!!)
                    handleAction(DbAction.EDIT, storedLocation)
                }
            }
        }
    }

    private fun deleteLocation(locationEntity: LocationEntity) {
        mainViewModel.deleteLocation(locationRepository, locationEntity)
        setStoredLocations()
    }

    private fun setStoredLocations() {
        lifecycleScope.launch {
            delay(500)
            databaseViewModel.postLocations(locationRepository.getAllLocations())
        }
    }

    private fun handleAction(action: DbAction, locationEntity: LocationEntity) {
        val inputEditTextField = EditText(this)
        inputEditTextField.setText(locationEntity.locationName.toString())
        val dialog =
            AlertDialog.Builder(this).setTitle(resources.getString(R.string.location_alert_title))
                .setMessage(resources.getString(R.string.location_alert_desc))
                .setView(inputEditTextField)
                .setPositiveButton(resources.getString(R.string.location_ok_action)) { _, _ ->
                    val input = inputEditTextField.text.toString()
                    val location = handleLocation(action, locationEntity, input)
                    databaseViewModel.handleLocationActions(action, locationRepository, location)
                    setStoredLocations()
                }
                .setNegativeButton(resources.getString(R.string.location_alert_cancel_action), null)
                .create()
        dialog.show()
    }

    private fun handleCoordinates() {
        mainViewModel.observeCoordinates(this) { location ->
            binding.progressBar.visibility = if (location == null) View.VISIBLE else View.GONE
            lifecycleScope.launch {
                val response = mainViewModel.makeMainRequest(location, phoneLanguage)
                mainViewModel.postMainResponse(response)
            }
        }
    }

    private fun showNetError() {
        Toast(this).setNotificationToast(
            R.string.location_toast_error_title,
            R.string.connection_error,
            R.color.color_danger,
            this,
            Toast.LENGTH_SHORT
        )
    }
}