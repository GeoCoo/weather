package coo.apps.weather.activities

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import coo.apps.weather.R
import coo.apps.weather.databinding.ActivityMainBinding


class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.label.toString()) {
                "Home" -> {
                    navView.menu.findItem(R.id.navigation_home).setIcon(R.drawable.ic_home_active)
                    navView.menu.findItem(R.id.navigation_maps).setIcon(R.drawable.ic_location_inactive)
                    navView.menu.findItem(R.id.navigation_charts).setIcon(R.drawable.ic_charts_inactive)


                }
                "Maps" -> {
                    navView.menu.findItem(R.id.navigation_home).setIcon(R.drawable.ic_home_inactive)
                    navView.menu.findItem(R.id.navigation_maps).setIcon(R.drawable.ic_location_active)
                    navView.menu.findItem(R.id.navigation_charts).setIcon(R.drawable.ic_charts_inactive)


                }
                "Charts" -> {
                    navView.menu.findItem(R.id.navigation_home).setIcon(R.drawable.ic_home_inactive)
                    navView.menu.findItem(R.id.navigation_charts).setIcon(R.drawable.ic_charts_active)
                    navView.menu.findItem(R.id.navigation_maps).setIcon(R.drawable.ic_location_inactive)


                }


            }

        }

    }
}