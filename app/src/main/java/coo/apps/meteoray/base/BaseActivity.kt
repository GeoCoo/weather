package coo.apps.meteoray.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import coo.apps.meteoray.locationsDb.LocationsRepository
import coo.apps.meteoray.viemodels.DatabaseViewModel
import coo.apps.meteoray.viemodels.MainViewModel
import coo.apps.meteoray.viemodels.NavigationViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

open class BaseActivity : AppCompatActivity() {

    protected lateinit var phoneLanguage: String

    protected val locationRepository: LocationsRepository by inject()
    protected val mainViewModel: MainViewModel by viewModel()
    protected val databaseViewModel: DatabaseViewModel by viewModel()
    protected val navigation: NavigationViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        phoneLanguage = getLanguage()
        lifecycleScope.launch {
            mainViewModel.postLimits()
        }
    }


    private fun getLanguage(): String {
        return Locale.getDefault().language
    }


}