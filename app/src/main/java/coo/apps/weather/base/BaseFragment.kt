package coo.apps.weather.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import coo.apps.weather.R
import coo.apps.weather.viemodels.LocationsViewModel
import coo.apps.weather.viemodels.MainViewModel
import coo.apps.weather.viemodels.NavigationViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseFragment : Fragment() {

    protected val mainViewModel: MainViewModel by sharedViewModel()
    protected val locationViewModel: LocationsViewModel by sharedViewModel()
    val navigation: NavigationViewModel by sharedViewModel()

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


}