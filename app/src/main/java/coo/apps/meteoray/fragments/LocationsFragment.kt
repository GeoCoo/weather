package coo.apps.meteoray.fragments

import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coo.apps.meteoray.R
import coo.apps.meteoray.adapters.LocationsAdapter
import coo.apps.meteoray.base.BaseFragment
import coo.apps.meteoray.databinding.FragmentLocationsBinding
import coo.apps.meteoray.locationsDb.LocationEntity
import coo.apps.meteoray.models.DbAction
import coo.apps.meteoray.models.NavigationDest


class LocationsFragment : BaseFragment() {

    private lateinit var locationsAdapter: LocationsAdapter
    private lateinit var backArrow: Drawable

    private var _binding: FragmentLocationsBinding? = null
    private val binding get() = _binding!!

    private var dbActions: (Pair<DbAction, LocationEntity>) -> Unit = { item ->
        dataBaseViewModel.postDbAction(item)
    }

    override fun getLayoutRes() = R.layout.fragment_locations

    override fun initLayout(view: View) {
        setUpToolbar()
        getAllLocations()
        addNewLocation()
        (activity as AppCompatActivity?)!!.supportActionBar

    }

    private fun setUpToolbar() {
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar.apply {
            this?.show()
            this?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorWhite)))
            this?.title = resources.getString(R.string.title_locations)
            this?.setHomeButtonEnabled(true)
            this?.setDisplayHomeAsUpEnabled(true)
            backArrow =
                resources.getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            backArrow.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
            this?.setHomeAsUpIndicator(backArrow)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
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
            navigation.handleNavigation(NavigationDest.MAPS)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}