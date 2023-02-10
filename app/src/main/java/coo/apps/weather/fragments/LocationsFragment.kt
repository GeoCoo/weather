package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import coo.apps.weather.R
import coo.apps.weather.adapters.LocationsAdapter
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentLocationsBinding
import coo.apps.weather.databinding.FragmentMapsBinding
import coo.apps.weather.models.locationsDb.LocationRoom


class LocationsFragment : BaseFragment() {

    private lateinit var binding: FragmentLocationsBinding
    private lateinit var locationsAdapter: LocationsAdapter

    override fun getLayoutRes() = R.layout.fragment_locations

    override fun initLayout(view: View) {
//        setRecycler()
        getAllLocations()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getAllLocations() {
        locationViewModel.observeLocations(viewLifecycleOwner) { locations ->
            setupRecyclerAdapter(locations)
        }
    }

    private fun setupRecyclerAdapter(list: List<LocationRoom?>) {
        binding.apply {
            val layoutManager = LinearLayoutManager(activity)
            this.recycler.layoutManager = layoutManager
            this.recycler.setHasFixedSize(true);
            locationsAdapter = LocationsAdapter(list)
            this.recycler.adapter = locationsAdapter

//            locationsAdapter.onItemClick = {
//                Toast.makeText(activity,it?.locationName.toString(),Toast.LENGTH_SHORT).show()
//            }

        }
    }

    private fun setRecycler() {
        binding.apply {
            this?.recycler?.setHasFixedSize(true);
            this?.recycler?.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        }
    }
}