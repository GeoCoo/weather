package coo.apps.weather.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.cardview.widget.CardView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import coo.apps.weather.R
import coo.apps.weather.base.BaseActivity
import coo.apps.weather.databinding.ActivityMainBinding
import coo.apps.weather.models.NavigationDest
import coo.apps.weather.locationsDb.AppDatabase
import coo.apps.weather.locationsDb.LocationRoom
import coo.apps.weather.utils.isInBoundBox
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

        navView = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        bottomSheetBehavior = BottomSheetBehavior.from<CardView>(binding.bottomSheetView.actionsView)

        mainViewModel.observeCoordinates(this@MainActivity) { locatioon ->
            lifecycleScope.launch {
                if (mainViewModel.boundsMutable.value?.let { locatioon?.isInBoundBox(it) } == true) {
                    val response = mainViewModel.makeMainRequest(locatioon)
                    mainViewModel.postMainResponse(response)
                }
            }
        }
        handleBottomSheet()
        setListeners()
    }

    private fun handleBottomSheet() {
        mainViewModel.observeBottomSheetState(this) { bottomSheetState ->
            bottomSheetBehavior.state = bottomSheetState
        }
    }

    private fun setListeners(){
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
                navigation.handleNavigation(navView,NavigationDest.MAPS)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}