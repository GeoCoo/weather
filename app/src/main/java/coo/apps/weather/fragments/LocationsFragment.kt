package coo.apps.weather.fragments

import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coo.apps.weather.R
import coo.apps.weather.adapters.LocationsAdapter
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentLocationsBinding
import coo.apps.weather.locationsDb.LocationEntity
import coo.apps.weather.models.DbAction
import coo.apps.weather.models.NavigationDest


class LocationsFragment : BaseFragment() {

    private lateinit var binding: FragmentLocationsBinding
    private lateinit var locationsAdapter: LocationsAdapter

    private var dbActions: (Pair<DbAction, LocationEntity>) -> Unit = { item ->
        dataBaseViewModel.postDbAction(item)
    }

    override fun getLayoutRes() = R.layout.fragment_locations

    override fun initLayout(view: View) {
        setUpToolbar()
        getAllLocations()
        addNewLocation()

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getAllLocations() {
        dataBaseViewModel.observeLocations(viewLifecycleOwner) { locations ->
            setupRecyclerAdapter(locations)
        }
    }

    private fun setupRecyclerAdapter(list: List<LocationEntity?>) {
        binding.apply {
            this.recycler.setHasFixedSize(true)
            this.recycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            locationsAdapter = LocationsAdapter(list, dbActions)
            this.recycler.adapter = locationsAdapter
            locationsAdapter.notifyDataSetChanged()

        }
    }

    private fun addNewLocation() {
        binding.addNew.setOnClickListener {
//            dataBaseViewModel.observeSingleLocation(viewLifecycleOwner) {
//                dataBaseViewModel.postDbAction(Pair(DbAction.SAVE, it))
//            }

            navigation.handleNavigation(navView, NavigationDest.MAPS)
        }
    }
}