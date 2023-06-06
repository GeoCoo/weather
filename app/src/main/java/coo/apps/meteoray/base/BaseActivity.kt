package coo.apps.meteoray.base

import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import coo.apps.meteoray.R
import coo.apps.meteoray.activities.MainActivity
import coo.apps.meteoray.locationsDb.LocationsRepository
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        phoneLanguage = getLanguage()
        lifecycleScope.launch {
            mainViewModel.postLimits()
        }
    }


    private fun getLanguage(): String {
        return Locale.getDefault().language
    }


    fun Toast.setNotificationToast(
        title: Int,
        message: Int,
        color: Int,
        activity: AppCompatActivity
    ) {
        if (activity is MainActivity) {
            val layout = activity.layoutInflater.inflate(
                R.layout.notification_toast,
                activity.findViewById(R.id.toastContainer)
            )

            val toastText = layout.findViewById<TextView>(R.id.toastText)
            val toastCard = layout.findViewById<CardView>(R.id.toastCard)
            val toastTitle = layout.findViewById<TextView>(R.id.toastTitle)
            toastText.text = resources.getString(message)
            toastCard.setCardBackgroundColor(resources.getColor(color))
            toastTitle.text = resources.getString(title)

            this.apply {
                setGravity(Gravity.BOTTOM, 0, 40)
                duration = Toast.LENGTH_LONG
                view = layout
                show()
            }
        }
    }
}