package coo.apps.meteoray.base

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coo.apps.meteoray.locationsDb.LocationsRepository
import coo.apps.meteoray.managers.ConnectionLiveData
import coo.apps.meteoray.viemodels.DatabaseViewModel
import coo.apps.meteoray.viemodels.MainViewModel
import coo.apps.meteoray.viemodels.NavigationViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    protected lateinit var phoneLanguage: String
    protected val locationRepository: LocationsRepository by inject()
    protected val mainViewModel: MainViewModel by viewModel()
    protected val databaseViewModel: DatabaseViewModel by viewModel()
    protected val navigation: NavigationViewModel by viewModel()
    protected val checkConnection by lazy { ConnectionLiveData(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        phoneLanguage = getLanguage()
        checkConnection.observe(this@BaseActivity) { isConected ->
            lifecycleScope.launch {
                if (isConected == true) mainViewModel.postLimits()
            }
        }
    }
}

    private fun getLanguage(): String {
        return Locale.getDefault().language
    }