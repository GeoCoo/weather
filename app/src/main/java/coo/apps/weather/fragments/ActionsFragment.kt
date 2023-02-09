package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import coo.apps.weather.R
import coo.apps.weather.databinding.BottomsheetDialogBinding
import coo.apps.weather.models.NavigationDest
import coo.apps.weather.viemodels.MainViewModel
import coo.apps.weather.viemodels.NavigationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ActionsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetDialogBinding
    private val mainViewModel: MainViewModel by viewModel()
    private val navigation: NavigationViewModel by viewModel()
    private var navView: NavHostFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    //tasks that need to be done after the creation of Dialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navView =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        setUpNavigation()
    }

    private fun setUpNavigation() {
        binding.save.setOnClickListener {
            navigation.handleNavigation(navView, NavigationDest.LOCATIONS)
        }
        binding.view.setOnClickListener {
            navigation.handleNavigation(navView, NavigationDest.HOME)
        }
    }
}

