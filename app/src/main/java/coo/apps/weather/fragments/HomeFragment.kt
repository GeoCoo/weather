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
import coo.apps.weather.models.NavigationDest
import coo.apps.weather.models.main.*


class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var dailyAdapter: DailyRecyclerAdapter
    private lateinit var todayAdapter: TodayRecyclerAdapter


    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun initLayout(view: View) {
        setErrorView()
        mainViewModel.observeMainResponse(viewLifecycleOwner) {
            if (it != null) handleRequestView(it)
        }
        navigateToMaps(navView)
    }

    private fun handleRequestView(response: MainResponse) {
        binding.apply {
            this.mainView.main.visibility = View.VISIBLE
            this.errorView.error.visibility = View.GONE
        }
        setUpCurrent(response)
        initDailyRecycler(response.overview)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun setUpCurrent(response: MainResponse?) {
        binding.apply {
            forecastView.background = resources.getDrawable(getBg(response?.current?.bgclass!!))
            val weatherIcon = getIcon(response.current?.icon!!)
            mainView.wearherSymbol.setImageResource(weatherIcon)
            mainView.placeTxt.text = mainViewModel.getPlaceName()
            mainView.temperature.text = response.current?.temp
            mainView.weatherType.text = response.current?.desc
            mainView.humidity.text =
                resources.getString(R.string.humidity_tag, response.current?.relhum)
            mainView.rain.text = resources.getString(R.string.rain_tag, response.current?.precip)
            mainView.wind.text = resources.getString(
                R.string.wind_tag,
                response.current.wind10.toString(),
                response.current.wind10dir.toString()
            )
    //            mainView.dust.text = resources.getString(R.string.dust_tag, response.current?.dust)
    //            mainView.visibility.text =
    //                resources.getString(R.string.visibility_tag, response.current?.vis)
            setRecycler()
            setRadioBtn(this.mainView.toggle, response)
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

    private fun initTodayRecycler(list: List<DayTable>) {
        binding.apply {
            todayAdapter = TodayRecyclerAdapter(list)
            this?.mainView?.recycler?.adapter = todayAdapter
        }
    }

    private fun initDailyRecycler(list: List<Overview>) {
        binding.apply {
            dailyAdapter = DailyRecyclerAdapter(list)
            this.mainView.recycler.adapter = dailyAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        binding = null
    }


    private fun navigateToMaps(navHostFragment: NavHostFragment?) {
        binding.locationView.setOnClickListener {
            navigation.handleNavigation(navHostFragment, NavigationDest.MAPS)
        }
    }


    private fun setErrorView() {
        binding.apply {
            this.errorView.error.visibility = View.VISIBLE
            this.forecastView.background = resources.getDrawable(R.drawable.splash_bg)
        }

    }

    private fun setRecycler() {
        binding.apply {
            this?.mainView?.recycler?.setHasFixedSize(true);
            this?.mainView?.recycler?.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        }

    }


}