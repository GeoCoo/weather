package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import coo.apps.weather.R
import coo.apps.weather.adapters.DailyRecyclerAdapter
import coo.apps.weather.adapters.TodayRecyclerAdapter
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentHomeBinding
import coo.apps.weather.models.main.*


class HomeFragment : BaseFragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var dailyAdapter: DailyRecyclerAdapter
    private lateinit var todayAdapter: TodayRecyclerAdapter
    private var navView: NavHostFragment? = null


    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun initLayout(view: View) {
        navView = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        setErrorView()
        mainViewModel.observeMainResponse(viewLifecycleOwner) {
            if (it != null)   handleRequestView(it)
        }
        clickOnLocation(navView)
    }

    private fun handleRequestView(response: MainResponse) {
        binding?.mainView?.visibility = View.VISIBLE
        binding?.errorView?.visibility = View.GONE
        setUpCurrent(response)
        initDailyRecycler(response.overview)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    private fun setUpCurrent(response: MainResponse?) {
        binding?.apply {
            forecastView.background = resources.getDrawable(getBg(response?.current?.bgclass!!))
            val weatherIcon = getIcon(response.current?.icon!!)
            wearherSymbol.setImageResource(weatherIcon)
            placeTxt.text = mainViewModel.getPlaceName()
            temperature.text = response.current?.temp
            weatherType.text = response.current?.desc
            humidity.text = resources.getString(R.string.humidity_tag, response.current?.relhum)
            rain.text = resources.getString(R.string.rain_tag, response.current?.precip)
            wind.text = resources.getString(
                R.string.wind_tag,
                response.current?.wind10.toString(),
                response.current?.wind10dir.toString()
            )
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
            this?.recycler?.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initDailyRecycler(list: ArrayList<Overview>) {
        binding.apply {
            dailyAdapter = DailyRecyclerAdapter(list)
            this?.recycler?.adapter = dailyAdapter
            this?.recycler?.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    private fun clickOnLocation(navHostFragment: NavHostFragment?) {
        binding?.locationView?.setOnClickListener {
            mainViewModel.handleNavigation(navHostFragment, R.id.navigation_maps)
        }
    }


    private fun setErrorView() {
        binding?.errorView?.visibility = View.VISIBLE
        binding?.forecastView?.background = resources.getDrawable(R.drawable.splash_bg)
    }

}