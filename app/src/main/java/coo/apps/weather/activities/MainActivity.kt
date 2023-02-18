package coo.apps.weather.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import coo.apps.weather.R
import coo.apps.weather.base.BaseActivity
import coo.apps.weather.databinding.ActivityMainBinding
import coo.apps.weather.locationsDb.LocationEntity
import coo.apps.weather.models.DbAction
import coo.apps.weather.models.NavigationDest
import coo.apps.weather.utils.isInBoundBox
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : BaseActivity(), OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var navView: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        bottomSheetBehavior =
            BottomSheetBehavior.from<CardView>(binding.bottomSheetView.actionsView)
        setLocations()
        handleCoordinates()
        handleBottomSheet()
        setListeners()
        handleDbActions()
    }

    private fun setLocations() {
        lifecycleScope.launch() {
            delay(500)
            databaseViewModel.postLocations(locationRepository.getAllLocations())
        }
    }

    private fun getSingLocation(id: Int): LocationEntity = locationRepository.getSingleLocation(id)


    private fun deleteLocation(locationEntity: LocationEntity) {
        locationRepository.deleteLocation(location = locationEntity)
        setLocations()
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
                    val location = getSingLocation(location.uid!!)
                    handleAction(DbAction.EDIT, location)
                }
            }
        }
    }

    private fun handleCoordinates() {
        mainViewModel.observeCoordinates(this@MainActivity) { locatioon ->
            lifecycleScope.launch {
                if (mainViewModel.boundsMutable.value?.let { locatioon?.isInBoundBox(it) } == true) {
                    val response = mainViewModel.makeMainRequest(locatioon)
                    mainViewModel.postMainResponse(response)
                }
            }
        }
    }

    private fun handleBottomSheet() {
        mainViewModel.observeBottomSheetState(this) { bottomSheetState ->
            bottomSheetBehavior.state = bottomSheetState
        }
    }

    private fun setListeners() {
        binding.bottomSheetView.view.setOnClickListener(this)
        binding.bottomSheetView.save.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        when (view) {
            binding.bottomSheetView.view -> {
                navigation.handleNavigation(navView, NavigationDest.HOME)
            }
            binding.bottomSheetView.save -> {
                navigation.handleNavigation(navView, NavigationDest.LOCATIONS)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigation.handleNavigation(navView, NavigationDest.MAPS)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleAction(action: DbAction, locationEntity: LocationEntity) {
        val inputEditTextField = EditText(this)
        inputEditTextField.setText(locationEntity.locationName.toString())
        val dialog =
            AlertDialog.Builder(this).setTitle("Title").setMessage("Message")
                .setView(inputEditTextField).setPositiveButton("OK") { _, _ ->
                    val input = inputEditTextField.text.toString()
                    val location = handleLocation(action, locationEntity, input)
                    databaseViewModel.handleLocationActions(action, locationRepository, location)
                    setLocations()
                }.setNegativeButton("Cancel", null).create()
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


}