package coo.apps.weather.fragments

import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentMapsBinding
import coo.apps.weather.utils.createBoundBox


class MapsFragment : BaseFragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapsBinding
    var latLong: Location? = null

    override fun getLayoutRes(): Int = R.layout.fragment_maps

    override fun initLayout(view: View) {
        clearSearch()
        handleTextWatcher()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        setMapSettings(googleMap)
        createBox(googleMap)
        addNewMarkerOnclick(googleMap)
        initMarker(googleMap)

    }

    private fun addNewMarkerOnclick(googleMap: GoogleMap) {
        googleMap.setOnMapLongClickListener {
            googleMap.clear()
            handleNewLocation(it)
            val positionName = mainViewModel.getPlaceName()

            googleMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .title(positionName)
            )
        }
    }

    private fun initMarker(googleMap: GoogleMap) {
        mainViewModel.observeCoordinates(viewLifecycleOwner) { location ->
            if (location != null) {
                val lebanon = LatLng(location.latitude, location.longitude)
                googleMap.addMarker(MarkerOptions().position(lebanon).title("coo.apps.weather.models.weather.Marker in lebanon").draggable(true))
            }
        }
    }

    private fun createBox(gooleMap: GoogleMap) {
        mainViewModel.observeBounds(requireActivity()) { limits ->
            val bounds = limits.createBoundBox()

            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val padding = (width * 0.12).toInt() // offset from edges of the map 12% of screen

            val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
            gooleMap.apply {
                this.animateCamera(cu)
            }

        }
    }

    private fun handleTextWatcher() {
        binding.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text?.length == 0) binding.searchIcon.setImageDrawable(context?.getDrawable(R.drawable.ic_search))
                else binding.searchIcon.setImageDrawable(context?.getDrawable(R.drawable.ic_location))
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }


    private fun clearSearch() {
        binding.clear.setOnClickListener {
            binding.searchField.text?.clear()
        }
    }


    private fun setMapSettings(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
    }


    private fun handleNewLocation(newLocation: LatLng) {
        val location = Location("")
        location.latitude = newLocation.latitude
        location.longitude = newLocation.longitude
        mainViewModel.postCoordinates(location)
    }


}