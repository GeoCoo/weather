package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentHomeBinding
import coo.apps.weather.viemodels.HomeViewModel
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : BaseFragment() {

    private var binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by sharedViewModel()

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun initLayout(view: View) {
        runBlocking {
            mainViewModel.observeCoordinates(viewLifecycleOwner) { location ->
                homeViewModel.makeMainRequest(location)
            }
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}