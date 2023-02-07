package coo.apps.weather.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import coo.apps.weather.R
import coo.apps.weather.base.BaseActivity
import coo.apps.weather.databinding.ActivityMainBinding
import coo.apps.weather.utils.isInBoundBox
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding


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
    }
}