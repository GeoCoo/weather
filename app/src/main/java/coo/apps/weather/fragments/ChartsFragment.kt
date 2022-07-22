package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentChartsBinding
import coo.apps.weather.viemodels.ChartsViewModel
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ChartsFragment : BaseFragment() {

    private var binding: FragmentChartsBinding? = null
    private val chartsViewModel: ChartsViewModel by sharedViewModel()

    override fun getLayoutRes(): Int = R.layout.fragment_charts
    override fun initLayout(view: View) {

        runBlocking {
            mainViewModel.observeCoordinates(viewLifecycleOwner) { location ->
                chartsViewModel.makeOceanRequest(location)
                chartsViewModel.makeWaveRequest(location)
                chartsViewModel.makeWeatherRequest(location)
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChartsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}