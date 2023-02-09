package coo.apps.weather.fragments

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import coo.apps.weather.R
import coo.apps.weather.adapters.DailyRecyclerAdapter
import coo.apps.weather.adapters.LocationsAdapter
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentHomeBinding
import coo.apps.weather.databinding.FragmentLocationsBinding
import coo.apps.weather.models.LocationsDb.LocationRoom


class LocationsFragment : BaseFragment() {

    private var binding: FragmentLocationsBinding ?= null
    private lateinit var adapter :LocationsAdapter

    override fun getLayoutRes() = R.layout.fragment_locations

    override fun initLayout(view: View) {

    }

    fun setupRecyclerAdapter(list:List<LocationRoom>) {
        binding.apply {

            this?.recycler?.setHasFixedSize(true);
            adapter = LocationsAdapter(list)
            this?.recycler?.adapter = adapter
            this?.recycler?.layoutManager = LinearLayoutManager(activity)

            adapter.onItemClick={

            }

        }
    }
}