package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.binders.abstraction.AdapterBinder
import coo.apps.weather.binders.abstraction.binders.RelativeHumidityChartBinder
import coo.apps.weather.databinding.FragmentChartsBinding
import coo.apps.weather.utils.convertToList
import coo.apps.weather.utils.getAdapterBinder
import coo.apps.weather.viemodels.ChartsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ChartsFragment : BaseFragment() {

    private lateinit var binding: FragmentChartsBinding
    private val chartsViewModel: ChartsViewModel by sharedViewModel()
    private var adapterBinder: AdapterBinder? = null

    override fun getLayoutRes(): Int = R.layout.fragment_charts
    override fun initLayout(view: View) {


        chartsViewModel.observeWeatherResponse(requireActivity()) {response->
            val list = response?.convertToList()
            initRecycler(list)
        }
    }

    private fun initRecycler(list: List<Any>?) {

        adapterBinder = getAdapterBinder(
            RelativeHumidityChartBinder()
        )

        binding.hiChartsRecycler.adapter = adapterBinder
        (binding.hiChartsRecycler.adapter as? AdapterBinder)?.submitList(list)
    }

//    private fun createRelativeHumidityChart(rehlum: ArrayList<List<Double>>?) {
//        val chartView = binding.hc
//        val options = HIOptions()
//        val chart = HIChart()
//        chart.type = "line"
//        options.chart = chart;
//        val title = HITitle()
//        title.text = "Demo chart"
//        options.title = title
//        val series = HILine()
//        series.data = rehlum
//        options.series = ArrayList(Collections.singletonList(series))
//        chartView.options = options;
//    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChartsBinding.inflate(inflater, container, false)
        return binding.root
    }
}