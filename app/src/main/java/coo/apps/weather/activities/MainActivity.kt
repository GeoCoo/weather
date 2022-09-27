package coo.apps.weather.activities

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import coo.apps.weather.R
import coo.apps.weather.base.BaseActivity
import coo.apps.weather.databinding.ActivityMainBinding
import coo.apps.weather.utils.isInBoundBox
import coo.apps.weather.viemodels.ChartsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val chartsViewModel: ChartsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        mainViewModel.handleNavigation(navController, navView)


        mainViewModel.observeCoordinates(this@MainActivity) { locatioon ->
            lifecycleScope.launch {
                if (locatioon?.isInBoundBox(mainViewModel.boundsMutable.value!!) == true) {
                    val response = mainViewModel.makeMainRequest(locatioon)
                    mainViewModel.postMainResponse(response)

                    chartsViewModel.makeOceanRequest(locatioon)
                    chartsViewModel.makeWaveRequest(locatioon)
                    chartsViewModel.handleWeather(locatioon)
                }

            }

        }


    }

}