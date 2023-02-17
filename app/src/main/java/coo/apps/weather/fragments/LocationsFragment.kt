package coo.apps.weather.fragments

import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coo.apps.weather.R
import coo.apps.weather.adapters.LocationsAdapter
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentLocationsBinding
import coo.apps.weather.locationsDb.Location
import coo.apps.weather.locationsDb.LocationRoom


class LocationsFragment : BaseFragment() {

    private lateinit var binding: FragmentLocationsBinding
    private lateinit var locationsAdapter: LocationsAdapter

    override fun getLayoutRes() = R.layout.fragment_locations

    override fun initLayout(view: View) {
        setUpToolbar()
        getAllLocations()
        getSingleLocation()

    }

    private fun setUpToolbar() {
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar.apply {
            this?.show()
            this?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorWhite)))

            this?.title = "Locations"
            this?.setHomeButtonEnabled(true)
            this?.setDisplayHomeAsUpEnabled(true)
            val backArrow =
                resources.getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            backArrow.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
            this?.setHomeAsUpIndicator(backArrow)
        }

    }


    private fun getSingleLocation() {
        locationViewModel.observeSingleLocation(this) { location ->
            handleAddNewLocation(location)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getAllLocations() {
        val locations = locationRepository.getAllLocations()
        setupRecyclerAdapter(locations)


    }

    private fun setupRecyclerAdapter(list: List<LocationRoom?>) {
        binding.apply {
            this.recycler.setHasFixedSize(true);
            this.recycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            locationsAdapter = LocationsAdapter(list)
            this.recycler.adapter = locationsAdapter


        }
    }

    private fun handleAddNewLocation(location: Location) {
        binding.apply {
            addNew.setOnClickListener {
                val inputEditTextField = EditText(requireActivity())
                val dialog =
                    AlertDialog.Builder(requireContext()).setTitle("Title").setMessage("Message")
                        .setView(inputEditTextField).setPositiveButton("OK") { _, _ ->
                            val editTextInput = inputEditTextField.text.toString()
                            locationRepository.insertUser(
                                LocationRoom(
                                    locationName = editTextInput,
                                    locationLon = location.locationLon,
                                    locationLat = location.locationLat,
                                    uid = 0
                                )
                            )
                        }.setNegativeButton("Cancel", null).create()
                dialog.show()

            }
        }
    }


}