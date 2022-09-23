package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.highsoft.highcharts.common.hichartsclasses.HIChart
import com.highsoft.highcharts.common.hichartsclasses.HIColumn
import com.highsoft.highcharts.common.hichartsclasses.HIOptions
import com.highsoft.highcharts.common.hichartsclasses.HITitle
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentChartsBinding
import coo.apps.weather.models.weather.WeatherResponse
import coo.apps.weather.viemodels.ChartsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class ChartsFragment : BaseFragment() {

    private lateinit var binding: FragmentChartsBinding
    private val chartsViewModel: ChartsViewModel by sharedViewModel()

    override fun getLayoutRes(): Int = R.layout.fragment_charts
    override fun initLayout(view: View) {

        chartsViewModel.observeWeatherResponse(viewLifecycleOwner) {
            setFirstChart(it)
        }
    }

    private fun setFirstChart(weatherResponse: WeatherResponse?) {
        val chartView = binding.hc
        val options = HIOptions()

        val chart = HIChart()
        chart.type = "column"

        options.chart = chart;

        val title = HITitle()
        title.text = "Demo chart"

        options.title = title
        val series = HIColumn()
//        series.data = ArrayList(listOf(49.9, 71.5, 106.4, 129.2, 144, 176, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4))
        series.data = weatherResponse?.slp
        options.series = ArrayList(Collections.singletonList(series))
        chartView.options = options;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChartsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}