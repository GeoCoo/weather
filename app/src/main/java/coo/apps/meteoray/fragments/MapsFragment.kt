package coo.apps.meteoray.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import coo.apps.meteoray.BuildConfig
import coo.apps.meteoray.R
import coo.apps.meteoray.adapters.PlacesResultAdapter
import coo.apps.meteoray.base.BaseFragment
import coo.apps.meteoray.databinding.FragmentMapsBinding
import coo.apps.meteoray.locationsDb.LocationEntity
import coo.apps.meteoray.models.Notification
import coo.apps.meteoray.utils.createBoundBox
import coo.apps.meteoray.utils.createLocation
import coo.apps.meteoray.utils.createRect


open class MapsFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var bounds: LatLngBounds
    private lateinit var placeClient: PlacesClient
    private lateinit var selectedPlace: LatLng

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var map: GoogleMap? = null
    private var marker: Marker? = null
    private var placeAdapter: PlacesResultAdapter? = null

    override fun getLayoutRes(): Int = R.layout.fragment_maps

    override fun initLayout(view: View) {
        clearSearch()
        handleTextWatcher()
        handleSearchPlaces()
    }

    private fun handleSearchPlaces() {
        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
        placeClient = Places.createClient(requireContext())
        placeAdapter = PlacesResultAdapter(placeClient) { prediction ->
            binding.searchField.setText(prediction.getPrimaryText(null).toString())
            getPlaceLatLong(prediction)
            binding.placesRecycler.visibility = View.GONE
        }
        binding.placesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.placesRecycler.adapter = placeAdapter

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.apply {
            map = this
            setMapSettings(map)
            createBox(map)
            drawBounds(bounds, R.color.black, map)
            this.setOnMapLongClickListener {
                addNewMarkerOnclick(map, it)
            }
        }
    }


    private fun addNewMarkerOnclick(googleMap: GoogleMap?, latLng: LatLng) {
        if (mainViewModel.checkIfIsInBox(latLng)) {
            marker?.remove()
            handleNewLocation(latLng)
            val positionName = mainViewModel.getPlaceName()
            dataBaseViewModel.postSingleLocation(
                LocationEntity(
                    locationName = positionName,
                    locationLat = latLng.latitude,
                    locationLon = latLng.longitude,
                    uid = 0
                )
            )
            mainViewModel.postBottomSheetState(BottomSheetBehavior.STATE_EXPANDED)
            marker = googleMap?.addMarker(MarkerOptions().position(latLng).title(positionName))
        } else {
            mainViewModel.postBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
            mainViewModel.postSearchNotification(Notification.FAIL)
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

            override fun afterTextChanged(input: Editable?) {
                if (input.toString() != "") {
                    marker?.remove()
                    placeAdapter!!.filter.filter(input.toString())
                    if (binding.placesRecycler.visibility == View.GONE) {
                        binding.placesRecycler.visibility = View.VISIBLE;
                    }
                } else {
                    if (binding.placesRecycler.visibility == View.VISIBLE) {
                        binding.placesRecycler.visibility = View.GONE;
                    }
                }
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
        val location = newLocation.createLocation()
        mainViewModel.postCoordinates(location)
    }

    private fun drawBounds(bounds: LatLngBounds, color: Int, googleMap: GoogleMap?) {
        val polygonOptions = bounds.createRect(color)
        googleMap?.addPolygon(polygonOptions)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getPlaceLatLong(place: AutocompletePrediction) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(place.placeId, placeFields)
        placeClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                addNewMarkerOnclick(map, response.place.latLng as LatLng)
            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    val statusCode = exception.statusCode
                }
            }

    }

}