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
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentMapsBinding


class MapsFragment : BaseFragment(), OnMapReadyCallback {
    private var binding: FragmentMapsBinding? = null
    var latLong: Location? = null

    override fun getLayoutRes(): Int = R.layout.fragment_maps

    override fun initLayout(view: View) {
        clearSearch()
        handleTextWatcher()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    override fun onMapReady(googleMap: GoogleMap) {
        setMapSettings(googleMap)
        createBox(googleMap)


        mainViewModel.observeCoordinates(viewLifecycleOwner) { location ->
//            val lebanon = LatLng(location?.latitude!!, location?.longitude!!)
            val lebanon = LatLng(29.3117, 47.4818)

            googleMap.addMarker(MarkerOptions().position(lebanon).title("Marker in lebanon"))
        }

//        googleMap.apply {
//            val sydney = LatLng(29.3117, 47.4818)
//            addMarker(
//                MarkerOptions()
//                    .position(sydney)
//                    .title("Marker in Lebanon")
//            )
//            // [START_EXCLUDE silent]
//            moveCamera(CameraUpdateFactory.newLatLng(sydney))
//        }

    }

    private fun createBox(gooleMap: GoogleMap) {
        mainViewModel.observeBounds(requireActivity(), Observer {
            val builder = LatLngBounds.builder()
            builder.include(LatLng(it?.xmin!!, it.xmax!!))
            builder.include(LatLng(it.ymin!!, it.ymax))
            val bounds = builder.build()

            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val padding = (width * 0.12).toInt() // offset from edges of the map 12% of screen


            val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
            gooleMap.apply {
                this.animateCamera(cu)
            }

        })
    }

    private fun handleTextWatcher() {
        binding?.searchField?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text?.length == 0) binding?.searchIcon?.setImageDrawable(context?.getDrawable(R.drawable.ic_search))
                else binding?.searchIcon?.setImageDrawable(context?.getDrawable(R.drawable.ic_location))
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }


    private fun clearSearch() {
        binding?.clear?.setOnClickListener {
            binding?.searchField?.text?.clear()
        }
    }


    private fun setMapSettings(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
    }


}