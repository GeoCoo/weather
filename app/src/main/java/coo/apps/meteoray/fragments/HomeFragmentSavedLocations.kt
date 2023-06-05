package coo.apps.meteoray.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coo.apps.meteoray.R
import coo.apps.meteoray.adapters.DailyRecyclerAdapter
import coo.apps.meteoray.adapters.TodayRecyclerAdapter
import coo.apps.meteoray.base.BaseFragment
import coo.apps.meteoray.databinding.LocationItemPageBinding
import coo.apps.meteoray.locationsDb.LocationEntity
import coo.apps.meteoray.models.NavigationDest
import coo.apps.meteoray.models.main.DayTable
import coo.apps.meteoray.models.main.MainResponse
import coo.apps.meteoray.models.main.Overview
import coo.apps.meteoray.models.main.getBg
import coo.apps.meteoray.models.main.getIcon
import coo.apps.meteoray.utils.OnSwipeTouchListener
import kotlinx.coroutines.launch


class HomeFragmentSavedLocations : BaseFragment() {

    private var _binding: LocationItemPageBinding? = null
    private val binding get() = _binding!!
    var pagePosition: Int = 0
    private lateinit var dailyAdapter: DailyRecyclerAdapter
    private lateinit var todayAdapter: TodayRecyclerAdapter
    private lateinit var locations: List<LocationEntity?>

    override fun getLayoutRes(): Int = R.layout.location_item_page

    override fun initLayout(view: View) {

        navigateToMaps()
        navigateToSettings()
        mainViewModel.postPagerPosition(pagePosition)

        locations = locationRepository.getAllLocations()


        repeat(locations.size) {
            binding.indicator.addTab(binding.indicator.newTab().setIcon(R.drawable.dot_unselected))
        }




        mainViewModel.observePagerPosition(viewLifecycleOwner) { position ->

            binding.indicator.getTabAt(position)?.setIcon(R.drawable.dot_selected)
        }

        if (locations.isNotEmpty()) {
            binding.mainView.main.visibility = View.VISIBLE
        }





        mainViewModel.observeMainResponse(viewLifecycleOwner) { response ->

            if (response != null) {
                setRecycler()
                setUpCurrent(response)
                initDailyRecycler(response.overview)
            }
        }

        binding.forecastView.setOnTouchListener(object : OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                if (pagePosition in 0 until locations.size - 1) {
                    pagePosition += 1
                    mainViewModel.postPagerPosition(pagePosition)
                    binding.indicator.getTabAt(pagePosition - 1)?.setIcon(R.drawable.dot_unselected)

                }
            }

            override fun onSwipeRight() {
                if (pagePosition in 1 until locations.size) {
                    pagePosition -= 1
                    mainViewModel.postPagerPosition(pagePosition)
                    binding.indicator.getTabAt(pagePosition + 1)?.setIcon(R.drawable.dot_unselected)

                }
            }
        })

    }


    private fun navigateToMaps() {
        binding.locationView.setOnClickListener {
            navigation.postNavigation(NavigationDest.MAPS)
        }
    }

    private fun navigateToSettings() {
        binding.settingsView.setOnClickListener {
            navigation.postNavigation(NavigationDest.SETTINGS)
            navigation.postDestinationNav(R.string.title_settings)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = LocationItemPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecycler() {
        binding.apply {
            this.mainView.recycler.setHasFixedSize(true)
            this.mainView.recycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initTodayRecycler(list: List<DayTable>) {
        binding.apply {
            todayAdapter = TodayRecyclerAdapter(list)
            this.mainView.recycler.adapter = todayAdapter
        }
    }

    private fun initDailyRecycler(list: List<Overview>) {
        binding.apply {
            dailyAdapter = DailyRecyclerAdapter(list)
            this.mainView.recycler.adapter = dailyAdapter
        }
    }

    private fun setUpCurrent(response: MainResponse) {
        binding.apply {
            forecastView.background = resources.getDrawable(getBg(response.current.bgclass!!))
            val weatherIcon = response.current.icon.let { getIcon(it) }
            weatherIcon.let { mainView.wearherSymbol.setImageResource(it) }
            lifecycleScope.launch {
                mainView.placeTxt.text = locations[pagePosition]?.locationName
            }

            if (getSharedPref("fahreneit")) {
                mainView.temperature.text = response.current.tempfrt
                mainView.temperatureType.text = resources.getString(R.string.fahreneit_symbol)
            } else {
                mainView.temperature.text = response.current.temp
                mainView.temperatureType.text = resources.getString(R.string.celcius_symbol)
            }

            mainView.weatherType.text = response.current.desc
            mainView.humidity.text =
                resources.getString(R.string.humidity_tag, response.current.relhum)
            mainView.rain.text = resources.getString(R.string.rain_tag, response.current.precip)
            if (getSharedPref("bofor")) {
                mainView.wind.text = resources.getString(
                    R.string.wind_tag,
                    response.current.wind10bft.toString(),
                    response.current.wind10dir.toString()
                )
            } else {
                mainView.wind.text = resources.getString(
                    R.string.wind_tag,
                    response.current.wind10.toString(),
                    response.current.wind10dir.toString()
                )
            }
            mainView.hail.text = resources.getString(R.string.hail_tag, response.current.hail)
            mainView.sunset.text = resources.getString(R.string.sunset_tag, response.current.up)
            mainView.sunrise.text = resources.getString(
                R.string.sunrise_tage, response.current.down
            )
            setRadioBtn(this.mainView.toggle, response)
        }
    }

    private fun setRadioBtn(toggle: RadioGroup, response: MainResponse) {
        toggle.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.daily -> {
                    initDailyRecycler(response.overview)
                }

                R.id.hourly -> {
                    initTodayRecycler(response.dayTable)
                }
            }
        }
    }


}