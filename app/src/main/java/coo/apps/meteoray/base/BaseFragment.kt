package coo.apps.meteoray.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import coo.apps.meteoray.R
import coo.apps.meteoray.locationsDb.LocationsRepository
import coo.apps.meteoray.viemodels.DatabaseViewModel
import coo.apps.meteoray.viemodels.MainViewModel
import coo.apps.meteoray.viemodels.NavigationViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment : Fragment() {

    protected val mainViewModel: MainViewModel by sharedViewModel()
    protected val dataBaseViewModel: DatabaseViewModel by sharedViewModel()
    protected val navigation: NavigationViewModel by sharedViewModel()
    protected val locationRepository: LocationsRepository by inject()
    protected lateinit var actionBar: ActionBar

    var navView: NavHostFragment? = null

    abstract fun getLayoutRes(): Int
    abstract fun initLayout(view: View)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navView =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
        (activity as AppCompatActivity?)!!.supportActionBar.let {
            if (it != null) {
                actionBar = it
            }
        }
        initLayout(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onPause() {
        super.onPause()
    }


    fun setFahreneitChoice(choice: Boolean) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(resources.getString(R.string.fahreneit_choice), choice)
            apply()
        }
    }

    fun setBofortChoice(choice: Boolean) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(resources.getString(R.string.bofohrt_choice), choice)
            apply()
        }
    }


    fun getSharedPref(value: String): Boolean {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return false
        return when (value) {
            "fahreneit" -> {
                sharedPref.getBoolean(getString(R.string.fahreneit_choice), false)
            }
            "bofor" -> {
                sharedPref.getBoolean(getString(R.string.bofohrt_choice), false)

            }
            else -> false
        }
    }


}