package coo.apps.weather.fragments

import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentMapsBinding
import coo.apps.weather.locationsDb.LocationEntity
import coo.apps.weather.utils.createBoundBox
import coo.apps.weather.utils.createRect


class MapsFragment : BaseFragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapsBinding
    private var map: GoogleMap? = null
    private lateinit var bounds: LatLngBounds
    private var marker: Marker? = null

    override fun getLayoutRes(): Int = R.layout.fragment_maps

    override fun initLayout(view: View) {
        clearSearch()
        handleTextWatcher()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        googleMap.apply {
            map = this
            setMapSettings(map)
            createBox(map)
            drawBounds(bounds, R.color.black, map)
            addNewMarkerOnclick(map)

        }
    }


    private fun addNewMarkerOnclick(googleMap: GoogleMap?) {
        googleMap?.apply {
            setOnMapLongClickListener {
                if (mainViewModel.checkIfIsInBox(it)) {
                    marker?.remove()
                    handleNewLocation(it)
                    val positionName = mainViewModel.getPlaceName()
                    dataBaseViewModel.postSingleLocation(
                        LocationEntity(
                            locationName = positionName,
                            locationLat = it.latitude,
                            locationLon = it.longitude,
                            uid = 0
                        )
                    )
                    mainViewModel.postBottomSheetState(BottomSheetBehavior.STATE_EXPANDED)
                    marker = this.addMarker(MarkerOptions().position(it).title(positionName))
                } else {
                    mainViewModel.postBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
                }


            }
        }
    }


//    private fun initMarker(googleMap: GoogleMap) {
//        mainViewModel.observeCoordinates(viewLifecycleOwner) { location ->
//            if (location != null) {
//                val lebanon = LatLng(location.latitude, location.longitude)
//                googleMap.addMarker(
//                    MarkerOptions().position(lebanon)
//                        .title("coo.apps.weather.models.weather.Marker in lebanon").draggable(true)
//                )
//            }
//        }
//    }

    private fun createBox(googleMap: GoogleMap?) {
        mainViewModel.observeBounds(requireActivity()) { limits ->
            bounds = limits.createBoundBox()
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val padding = (width * 0.12).toInt() // offset from edges of the map 30% of screen

            val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
            googleMap.apply {
                this?.animateCamera(cu)
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


    private fun setMapSettings(googleMap: GoogleMap?) {
        googleMap?.apply {
            this.uiSettings.isZoomControlsEnabled = true
            this.uiSettings.isScrollGesturesEnabled = true
        }

    }

    private fun handleNewLocation(newLocation: LatLng) {
        val location = Location("")
        location.latitude = newLocation.latitude
        location.longitude = newLocation.longitude
        mainViewModel.postCoordinates(location)
    }

    private fun drawBounds(bounds: LatLngBounds, color: Int, googleMap: GoogleMap?) {
        val polygonOptions = bounds.createRect(color)
        googleMap?.addPolygon(polygonOptions)
    }

}