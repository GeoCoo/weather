package coo.apps.weather.activities

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import coo.apps.weather.base.BaseActivity
import coo.apps.weather.databinding.ActivityMainBinding
import coo.apps.weather.models.LocationsDb.AppDatabase
import coo.apps.weather.models.LocationsDb.LocationDao
import coo.apps.weather.utils.isInBoundBox
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private var locationDao: LocationDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.observeCoordinates(this@MainActivity) { locatioon ->
            lifecycleScope.launch {
                if (mainViewModel.boundsMutable.value?.let { locatioon?.isInBoundBox(it) } == true) {
                    val response = mainViewModel.makeMainRequest(locatioon)
                    mainViewModel.postMainResponse(response)
                }
            }
        }


        setUpDao()
    }

    private fun setUpDb() = Room.databaseBuilder(this, AppDatabase::class.java, "Locations").build()

    private fun setUpDao(){
      locationDao = setUpDb().locationDao()
    }

    fun getAllLocations(dao:LocationDao){
        locationViewModel.observeLocations(this@MainActivity){

        }
    }



}