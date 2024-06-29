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
import coo.apps.meteoray.databinding.FragmentHomeBinding
import coo.apps.meteoray.locationsDb.LocationEntity
import coo.apps.meteoray.models.NavigationDest
import coo.apps.meteoray.models.main.DayTable
import coo.apps.meteoray.models.main.MainResponse
import coo.apps.meteoray.models.main.Overview
import coo.apps.meteoray.models.main.getBg
import coo.apps.meteoray.models.main.getIcon
import coo.apps.meteoray.utils.OnSwipeTouchListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dailyAdapter: DailyRecyclerAdapter
    private lateinit var todayAdapter: TodayRecyclerAdapter
    var pagePosition: Int = 0

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun initLayout(view: View) {
        val locations = locationRepository.getAllLocations()
        navigateToSettings()
        checkConnection.observe(viewLifecycleOwner) { isConnected ->
            showLocations(isConnected, locations)
            if (isConnected == true) {
                navigateToMaps()
            } else {
                setNoNetView()
            }

        }

        if (locations.isNotEmpty()) {
            repeat(locations.size) {
                binding.indicator.addTab(
                    binding.indicator.newTab().setIcon(R.drawable.dot_unselected)
                )
            }

        }
    }

    private fun showLocations(isConnected: Boolean?, locations: List<LocationEntity?>) {
//        navigation.observeNavigation(lifecycleOwner = viewLifecycleOwner) { item ->
//            if (item?.second != null) {
//                handleNoLocations()
//            }
//        }
        when {
            isConnected == true && locations.isEmpty() -> {
                handleNoLocations()
            }

            isConnected == true && locations.isNotEmpty() -> {
                handleLocations(locations)
            }

            isConnected == false && locations.isNotEmpty() -> {
                setNoNetView()
            }

            isConnected == false || isConnected == null -> {
                setNoNetView()
            }
        }


    }

    private fun handleLocations(locations: List<LocationEntity?>) {
        binding.indicator.visibility = View.VISIBLE
        binding.mainView.main.visibility = View.VISIBLE
        binding.errorView.error.visibility = View.GONE
        binding.errorView.error.visibility = View.GONE

        mainViewModel.observePagerPosition(viewLifecycleOwner) { position ->
            binding.indicator.getTabAt(position)?.setIcon(R.drawable.dot_selected)

            binding.forecastView.setOnTouchListener(object : OnSwipeTouchListener() {
                override fun onSwipeLeft() {
                    if (pagePosition in 0 until locations.size - 1) {
                        pagePosition += 1
                        mainViewModel.postPagerPosition(pagePosition)
                        binding.indicator.getTabAt(pagePosition - 1)
                            ?.setIcon(R.drawable.dot_unselected)

                    }
                }

                override fun onSwipeRight() {
                    if (pagePosition in 1 until locations.size) {
                        pagePosition -= 1
                        mainViewModel.postPagerPosition(pagePosition)
                        binding.indicator.getTabAt(pagePosition + 1)
                            ?.setIcon(R.drawable.dot_unselected)

                    }
                }
            }
            )

            mainViewModel.observeMainResponse(viewLifecycleOwner) {
                if (it != null)
                    handleRequestView(it, position)
            }

        }
    }

    private fun handleNoLocations() {
        binding.indicator.visibility = View.GONE

        mainViewModel.observeMainResponse(viewLifecycleOwner) {
            if (it != null) {
                handleRequestView(it, null)
            } else setErrorView()

        }
        binding.progressBar.visibility = View.GONE

    }

    private fun handleRequestView(response: MainResponse, position: Int?) {
        binding.apply {
            this.mainView.main.visibility = View.VISIBLE
            this.errorView.error.visibility = View.GONE
            this.progressBar.visibility = View.GONE

        }
        setRecycler()
        setUpCurrent(response, position)
        binding.mainView.toggle.check(R.id.hourly)
        initTodayRecycler(response.dayTable)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun setUpCurrent(response: MainResponse, position: Int?) {
        binding.apply {
            forecastView.background = resources.getDrawable(getBg(response.current.bgclass))
            val weatherIcon = response.current.icon.let { getIcon(it) }
            weatherIcon.let { mainView.wearherSymbol.setImageResource(it) }
            lifecycleScope.launch {
                navigation.observeNavigation(viewLifecycleOwner) { item ->
                    if (item?.second != null) {
                        mainView.placeTxt.text = mainViewModel.getPlaceName()
                        binding.indicator.visibility = View.GONE
                        binding.forecastView.setOnTouchListener(null)

                    }
                }
                mainView.placeTxt.text = if (position == null)
                    mainViewModel.getPlaceName() else locationRepository.getAllLocations()
                    .reversed()[position]?.locationName
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

    private fun initTodayRecycler(list: List<DayTable>) {
        runBlocking {
            binding.apply {

                todayAdapter =
                    TodayRecyclerAdapter(list, getSharedPref("bofor"), getSharedPref("fahreneit"))
                this.mainView.recycler.adapter = todayAdapter

            }
        }

    }

    private fun initDailyRecycler(list: List<Overview>) {
        runBlocking {
            binding.apply {
                dailyAdapter =
                    DailyRecyclerAdapter(list, getSharedPref("bofor"), getSharedPref("fahreneit"))
                this.mainView.recycler.adapter = dailyAdapter
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun navigateToMaps() {
        binding.locationView.setOnClickListener {
            navigation.postNavigation(Pair(NavigationDest.MAPS, null))
        }
    }

    private fun navigateToSettings() {
        binding.settingsView.setOnClickListener {
            navigation.postNavigation(Pair(NavigationDest.SETTINGS, null))
            navigation.postDestinationNav(R.string.title_settings)
        }
    }

    private fun setErrorView() {
        binding.apply {
            this.progressBar.visibility = View.GONE
            this.errorView.error.visibility = View.VISIBLE
            this.errorView.text.text = resources.getString(R.string.errorMsg)
            this.forecastView.background = resources.getDrawable(R.drawable.splash_bg)
            this.errorView.gotoMaps.visibility = View.VISIBLE
            this.mainView.main.visibility = View.GONE
            this.errorView.gotoMaps.setOnClickListener {
                navigation.postNavigation(Pair(NavigationDest.MAPS, null))
            }
        }
    }

    private fun setNoNetView() {
        binding.apply {
            this.errorView.error.visibility = View.VISIBLE
            this.errorView.text.text = resources.getString(R.string.connection_error)
            this.errorView.gotoMaps.visibility = View.GONE
            this.forecastView.visibility = View.VISIBLE
            this.progressBar.visibility = View.GONE
            this.mainView.main.visibility = View.GONE
            this.indicator.visibility = View.GONE
        }
    }

    private fun setRecycler() {
        binding.apply {
            this.mainView.recycler.setHasFixedSize(true)
            this.mainView.recycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}
