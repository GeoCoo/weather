package coo.apps.meteoray.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import coo.apps.meteoray.R
import coo.apps.meteoray.adapters.DailyRecyclerAdapter
import coo.apps.meteoray.adapters.TodayRecyclerAdapter
import coo.apps.meteoray.adapters.ViewPagerAdapter
import coo.apps.meteoray.base.BaseFragment
import coo.apps.meteoray.databinding.FragmentHomeBinding
import coo.apps.meteoray.models.NavigationDest
import coo.apps.meteoray.models.main.*


class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dailyAdapter: DailyRecyclerAdapter
    private lateinit var todayAdapter: TodayRecyclerAdapter
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun initLayout(view: View) {
        setErrorView()
        mainViewModel.observeMainResponse(viewLifecycleOwner) {
            if (it != null) handleRequestView(it)
        }
        navigateToMaps(navView)
        navigateToSettings(navView)


    }

    private fun handleRequestView(response: MainResponse) {
        binding.apply {
            this.mainView.main.visibility = View.VISIBLE
            this.errorView.error.visibility = View.GONE
        }
        setRecycler()
        setUpCurrent(response)
        initDailyRecycler(response.overview)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun setUpCurrent(response: MainResponse) {
        binding.apply {
            forecastView.background = resources.getDrawable(getBg(response?.current?.bgclass!!))
            val weatherIcon = response.current.icon.let { getIcon(it) }
            weatherIcon.let { mainView.wearherSymbol.setImageResource(it) }
            mainView.placeTxt.text = mainViewModel.getPlaceName()

            if (getSharedPref("fahreneit") == true) {
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
            if (getSharedPref("bofor") == true) {
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
                R.id.today -> {
                    initTodayRecycler(response.dayTable)

                }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun navigateToMaps(navHostFragment: NavHostFragment?) {
        binding.locationView.setOnClickListener {
            navigation.handleNavigation(navHostFragment, NavigationDest.MAPS)
        }
    }

    private fun navigateToSettings(navHostFragment: NavHostFragment?) {
        binding.settingsView.setOnClickListener {
            navigation.handleNavigation(navHostFragment, NavigationDest.SETTINGS)
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
            this.mainView.recycler.setHasFixedSize(true);
            this.mainView.recycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        }

    }


}