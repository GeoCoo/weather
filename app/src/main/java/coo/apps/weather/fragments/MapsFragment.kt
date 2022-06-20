package coo.apps.weather.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import coo.apps.weather.R
import coo.apps.weather.databinding.FragmentMapsBinding


class MapsFragment : Fragment() {
    private var binding: FragmentMapsBinding? = null

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        setMapSettings(googleMap)
        addMarkers(googleMap)
    }

    private fun addMarkers(googleMap: GoogleMap) {
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun setMapSettings(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        clearSearch()
        handleTextWatcher()
        return binding!!.root

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}


//private fun initPlaces() {
//
//    // Initialize the AutocompleteSupportFragment.
//    val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
//
//    // Specify the types of place data to return.
//    autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
//
//    // Set up a PlaceSelectionListener to handle the response.
//    autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//        override fun onPlaceSelected(place: Place) {
//            // TODO: Get info about the selected place.
////                Log.i(TAG, "Place: ${place.name}, ${place.id}"
//            Toast.makeText(requireActivity(), "Place: ${place.name}, ${place.id}", Toast.LENGTH_LONG).show()
//
//        }
//
//        override fun onError(status: Status) {
//            Toast.makeText(requireActivity(), status.statusMessage, Toast.LENGTH_LONG).show()
//
//        }
//    })
//}