package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentHomeBinding
import coo.apps.weather.models.main.*
import coo.apps.weather.utils.DailyRecyclerAdapter
import coo.apps.weather.utils.TodayRecyclerAdapter


class HomeFragment : BaseFragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var dailyAdapter: DailyRecyclerAdapter
    private lateinit var todayAdapter: TodayRecyclerAdapter
    private var hasResponse: Boolean = false

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun initLayout(view: View) {
        binding?.mainView?.visibility = View.GONE
        binding?.errorView?.visibility = View.VISIBLE
        mainViewModel.observeMainResponse(viewLifecycleOwner) {
            if (it != null) handleRequestView(it)
        }
    }

    private fun handleRequestView(response: MainResponse) {
        binding?.mainView?.visibility = View.VISIBLE
        binding?.errorView?.visibility = View.GONE
        setUpCurrent(response)
        initDailyRecycler(response.overview)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    private fun setUpCurrent(response: MainResponse?) {
        binding?.apply {
            weatherImage.setImageResource(getBg(response?.current?.bgclass!!))
            val weatherIcon = getIcon(response.current?.icon!!)
            wearherSymbol.setImageResource(weatherIcon)
            placeTxt.text = mainViewModel.getPlaceName()
            temperature.text = response.current?.temp
            weatherType.text = response.current?.desc
            humidity.text = resources.getString(R.string.humidity_tag, response.current?.relhum)
            rain.text = resources.getString(R.string.rain_tag, response.current?.precip)
            wind.text = resources.getString(R.string.wind_tag, response.current?.wind10.toString(), response.current?.wind10dir.toString())
            dust.text = resources.getString(R.string.dust_tag, response.current?.dust)
            visibility.text = resources.getString(R.string.visibility_tag, response.current?.vis)
            setRadioBtn(this.toggle, response)
        }


    }

    private fun setRadioBtn(toggle: RadioGroup, response: MainResponse) {
        toggle.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.daily -> {
                    initDailyRecycler(response.overview)
                }
                R.id.today -> {
                    initTodayRecycler(response.dayTable)

                }
            }
        }
    }

    private fun initTodayRecycler(list: ArrayList<DayTable>) {
        binding.apply {
            todayAdapter = TodayRecyclerAdapter(list)
            this?.recycler?.adapter = todayAdapter
            this?.recycler?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initDailyRecycler(list: ArrayList<Overview>) {
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