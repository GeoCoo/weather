package coo.apps.meteoray.activities

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import coo.apps.meteoray.R
import coo.apps.meteoray.base.BaseActivity
import coo.apps.meteoray.databinding.ActivityMainBinding
import coo.apps.meteoray.databinding.NotificationToastBinding
import coo.apps.meteoray.locationsDb.LocationEntity
import coo.apps.meteoray.models.DbAction
import coo.apps.meteoray.models.NavigationDest
import coo.apps.meteoray.models.Notification
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : BaseActivity(), OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var navView: NavHostFragment
    private lateinit var singleLocation: LocationEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        bottomSheetBehavior =
            BottomSheetBehavior.from<CardView>(binding.bottomSheetView.actionsView)
        setStoredLocations()
        handleCoordinates()
        handleBottomSheet()
        setBottomSheetListeners()
        handleDbActions()
        getLocationToBeStored()
        observerNavigation()

        mainViewModel.observeSearchNotification(this) {
            if (it == Notification.FAIL) {
                Toast(this).setNotificationToast(R.string.location_out_of_box, Color.RED)
            }
        }
    }

    private fun setStoredLocations() {
        lifecycleScope.launch() {
            delay(500)
            databaseViewModel.postLocations(locationRepository.getAllLocations())
        }
    }

    private fun deleteLocation(locationEntity: LocationEntity) {
        mainViewModel.deleteLocation(locationRepository, locationEntity)
        setStoredLocations()
    }

    private fun handleDbActions() {
        databaseViewModel.observeDbAction(this@MainActivity) { pair ->
            val (action, location) = pair
            when (action) {
                DbAction.SAVE -> {
                    handleAction(DbAction.SAVE, location)
                }
                DbAction.DELETE -> {
                    deleteLocation(location)
                }

                DbAction.EDIT -> {
                    val location =
                        mainViewModel.getSingleLocation(locationRepository, location.uid!!)
                    handleAction(DbAction.EDIT, location)
                }
            }
        }
    }

    private fun handleCoordinates() {
        mainViewModel.observeCoordinates(this@MainActivity) { locatioon ->
            lifecycleScope.launch {
//                if (mainViewModel.boundsMutable.value?.let { locatioon?.isInBoundBox(it) } == true) {
                val response = mainViewModel.makeMainRequest(locatioon, phoneLanguage)
                mainViewModel.postMainResponse(response)
//                }
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
                navigation.handleNavigation(NavigationDest.HOME)
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
            }
        }
    }

    private fun Toast.setNotificationToast(message: Int, color: Int) {
        var toastBinding: NotificationToastBinding =
            NotificationToastBinding.inflate(layoutInflater)

        // set the text of the TextView of the message
        toastBinding.toastText.text = resources.getString(message)
        toastBinding.toastContainer.setBackgroundColor(resources.getColor(color))

        // use the application extension function
        this.apply {
            setGravity(Gravity.BOTTOM, 0, 40)
            duration = Toast.LENGTH_LONG
            view = binding.root
            show()
        }
    }

}