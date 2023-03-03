package coo.apps.meteoray.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import coo.apps.meteoray.R
import coo.apps.meteoray.base.BaseActivity
import coo.apps.meteoray.databinding.ActivityMainBinding
import coo.apps.meteoray.locationsDb.LocationEntity
import coo.apps.meteoray.models.DbAction
import coo.apps.meteoray.models.NavigationDest
import coo.apps.meteoray.models.Notification
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : BaseActivity(), OnClickListener {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var navView: NavHostFragment
    private lateinit var singleLocation: LocationEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()


        navView =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        bottomSheetBehavior =
            BottomSheetBehavior.from<CardView>(binding.bottomSheetView.actionsView)
        isOnline(this)
        setStoredLocations()
        handleBottomSheet()
        setBottomSheetListeners()
        handleDbActions()
        getLocationToBeStored()
        observerNavigation()

        mainViewModel.observeSearchNotification(this) {
            if (it == Notification.FAIL) {
                Toast(this).setNotificationToast(
                    R.string.location_toast_error_title,
                    R.string.location_out_of_box,
                    R.color.color_danger, this
                )
            }
        }

    }








    private fun handleBottomSheet() {
        mainViewModel.observeBottomSheetState(this) { bottomSheetState ->
            bottomSheetBehavior.state = bottomSheetState
        }
    }

    private fun setBottomSheetListeners() {
        binding.bottomSheetView.view.setOnClickListener(this)
        binding.bottomSheetView.save.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        when (view) {
            binding.bottomSheetView.view -> {
                navigation.postNavigation(NavigationDest.HOME)
            }
            binding.bottomSheetView.save -> {
                databaseViewModel.postDbAction(Pair(DbAction.SAVE, singleLocation))
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
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        mainViewModel.postCoordinates(location)
                    }
                }

            } else {
                setLocationNotificationDialog()
//
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


    private fun isOnline(context: Context) {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null) {
            // connected to the internet
            when (activeNetwork.type) {
                ConnectivityManager.TYPE_WIFI -> {}
                ConnectivityManager.TYPE_MOBILE -> {}
                else -> {}
            }
        } else {
            Toast(this).setNotificationToast(
                R.string.location_toast_error_title,
                R.string.connection_error,
                R.color.color_danger,
                this
            )
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
        lifecycleScope.launch() {
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

}