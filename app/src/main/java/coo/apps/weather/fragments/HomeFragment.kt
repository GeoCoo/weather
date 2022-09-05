package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentHomeBinding
import coo.apps.weather.models.main.DayTable
import coo.apps.weather.models.main.MainResponse
import coo.apps.weather.models.main.Overview
import coo.apps.weather.utils.DailyRecyclerAdapter
import coo.apps.weather.utils.TodayRecyclerAdapter
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var response: MainResponse
    private lateinit var dailyAdapter: DailyRecyclerAdapter
    private lateinit var todayAdapter: TodayRecyclerAdapter

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun initLayout(view: View) {
        lifecycleScope.launch {
            response = mainViewModel.makeMainRequest()!!
            setUpCurrent(response)
            initDailyRecycler(response.overview)
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    private fun setUpCurrent(response: MainResponse?) {
        binding?.apply {
            placeTxt.text = mainViewModel.getPlaceName()
            temperature.text = response?.current?.temp
            weatherType.text = response?.current?.desc
            humidity.text = resources.getString(R.string.humidity_tag, response?.current?.relhum)
            rain.text = resources.getString(R.string.rain_tag, response?.current?.precip)
            wind.text = resources.getString(R.string.wind_tag, response?.current?.wind10, response?.current?.wind10dir)
            dust.text = resources.getString(R.string.dust_tag, response?.current?.dust)
            visibility.text = resources.getString(R.string.visibility_tag, response?.current?.vis)
            setRadioBtn(this.toggle, response)
        }


    }

    private fun setRadioBtn(toggle: RadioGroup, response: MainResponse?) {
        toggle.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { arg0, id ->
            when (id) {
                R.id.daily -> {

                    initDailyRecycler(response?.overview)
                }
                R.id.today -> {
                    initTodayRecycler(response?.dayTable)

                }
            }
        })
    }

    private fun initTodayRecycler(list: List<DayTable>?) {
        binding.apply {
            todayAdapter = TodayRecyclerAdapter(list)
            this?.recycler?.adapter = todayAdapter
            this?.recycler?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initDailyRecycler(list: List<Overview>?) {
        binding.apply {
            dailyAdapter = DailyRecyclerAdapter(list)
            this?.recycler?.adapter = dailyAdapter
            this?.recycler?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}