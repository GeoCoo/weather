package coo.apps.meteoray.fragments

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import coo.apps.meteoray.BuildConfig
import coo.apps.meteoray.R
import coo.apps.meteoray.adapters.PlacesResultAdapter
import coo.apps.meteoray.base.BaseFragment
import coo.apps.meteoray.databinding.FragmentMapsBinding
import coo.apps.meteoray.locationsDb.LocationEntity
import coo.apps.meteoray.models.DbAction
import coo.apps.meteoray.models.NavigationDest
import coo.apps.meteoray.models.Notification
import coo.apps.meteoray.utils.checkIfIsInBox
import coo.apps.meteoray.utils.createBoundBox
import coo.apps.meteoray.utils.createLocation
import coo.apps.meteoray.utils.createRect
import coo.apps.meteoray.utils.setNotificationToast
import kotlinx.coroutines.launch


open class MapsFragment : BaseFragment(), OnMapReadyCallback, OnMapLoadedCallback, OnClickListener {

    private lateinit var bounds: LatLngBounds
    private lateinit var positionName: String
    private lateinit var placeClient: PlacesClient
    private lateinit var singleLocation: LocationEntity

    private var placeAdapter: PlacesResultAdapter? = null
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null
    private var marker: Marker? = null

    override fun getLayoutRes(): Int = R.layout.fragment_maps

    override fun initLayout(view: View) {
        clearSearch()
        handleTextWatcher()
        handleSearchPlaces()
        setBottomSheetListeners()
        toggleActionView(false)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
            checkConnection.observe(viewLifecycleOwner) { isConnected ->
                if (isConnected == false) {
                    noNetToast()
                    map?.setOnMapClickListener { noNetToast() }
                    map?.setOnMapLongClickListener { noNetToast() }
                } else {

                    map?.setOnMapLongClickListener {
                        binding.searchField.text?.clear()
                        addNewMarkerOnclick(map, it)
                    }
                    map?.setOnMapClickListener {
                        toggleActionView(false)
                    }
                }

            }
            map = this
            setMapSettings(map)
            createBox(map)
            drawBounds(bounds, R.color.color_danger, map)


            this.setOnMapLoadedCallback(this@MapsFragment)
        }
    }

    private fun addNewMarkerOnclick(googleMap: GoogleMap?, latLng: LatLng) {
        mainViewModel.apply {
            this.observeBounds(viewLifecycleOwner) { bounds ->
                if (latLng.checkIfIsInBox(bounds) == true) {
                    marker?.remove()
                    handleNewLocation(latLng)
                    viewModelScope.launch {
                        positionName = this@apply.getPlaceName()
                    }
                    singleLocation = LocationEntity(
                        locationName = positionName,
                        locationLat = latLng.latitude,
                        locationLon = latLng.longitude,
                        uid = 0
                    )
                    dataBaseViewModel.postSingleLocation(singleLocation)
                    toggleActionView(true)
                    marker =
                        googleMap?.addMarker(MarkerOptions().position(latLng).title(positionName))
                } else {
                    toggleActionView(false)
                    this.postSearchNotification(Notification.FAIL)
                }
            }
        }

    }

    private fun createBox(googleMap: GoogleMap?) {
        mainViewModel.observeBounds(requireActivity()) { limits ->
            bounds = limits.createBoundBox()
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val padding = (width * 0.12).toInt()

            val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
            googleMap.apply {
                this?.animateCamera(cu)
            }
        }
    }

    private fun handleSearchPlaces() {
        binding.placesRecycler.setHasFixedSize(true)
        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
        placeClient = Places.createClient(requireContext())
        placeAdapter = PlacesResultAdapter { prediction ->
            binding.searchField.setText(prediction.getPrimaryText(null).toString())
            getPlaceLatLong(prediction)
            binding.placesRecycler.visibility = View.GONE
            val imm =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)

        }
        binding.placesRecycler.layoutManager = LinearLayoutManager(requireContext())
        (binding.placesRecycler.layoutManager as LinearLayoutManager).stackFromEnd
        binding.placesRecycler.adapter = placeAdapter
    }

    private fun handleTextWatcher() {
        binding.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filter(text.toString())
                handleRecyclerView(text?.length)
            }

            override fun afterTextChanged(text: Editable?) {

            }
        })
    }

    private fun handleRecyclerView(length: Int?) {
        if (length == 0) {
            binding.searchIcon.setImageDrawable(context?.getDrawable(R.drawable.ic_search))
            binding.clear.visibility = View.GONE
            if (binding.placesRecycler.visibility == View.VISIBLE) {
                binding.placesRecycler.visibility = View.GONE
            }
        } else {
            marker?.remove()
            binding.searchIcon.setImageDrawable(context?.getDrawable(R.drawable.ic_location))
            binding.clear.visibility = View.VISIBLE

            if (binding.placesRecycler.visibility == View.GONE) {
                binding.placesRecycler.visibility = View.VISIBLE
            }
        }
    }

    private fun clearSearch() {
        binding.clear.setOnClickListener {
            binding.placesRecycler.recycledViewPool.clear()
            binding.searchField.text?.clear()
        }
    }

    private fun setMapSettings(googleMap: GoogleMap?) {
        googleMap?.apply {
            this.uiSettings.isZoomControlsEnabled = true
            this.uiSettings.isScrollGesturesEnabled = true
            this.uiSettings.isMyLocationButtonEnabled = true
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
        placeClient.fetchPlace(request).addOnSuccessListener { response: FetchPlaceResponse ->
            addNewMarkerOnclick(map, response.place.latLng as LatLng)
            response.place.latLng?.let { zoomCameraToSelection(it, 12F) }
        }.addOnFailureListener { exception: Exception ->
            if (exception is ApiException) {
                val statusCode = exception.statusCode
            }
        }
    }

    private fun zoomCameraToSelection(latLng: LatLng, zoom: Float) {
        mainViewModel.observeBounds(viewLifecycleOwner) { bounds ->
            if (latLng.checkIfIsInBox(bounds) == true) {
                map?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            latLng.latitude, latLng.longitude
                        ), zoom
                    )
                )
            }
        }
    }


    private fun filter(text: String) {
        val result: ArrayList<AutocompletePrediction> = arrayListOf()
        val token = AutocompleteSessionToken.newInstance()
        val request =
            FindAutocompletePredictionsRequest.builder().setSessionToken(token).setQuery(text)
                .setCountry("GR")
                .build()

        placeClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                result.addAll(response.autocompletePredictions)
                placeAdapter?.filterList(result)
            }.addOnFailureListener {
                return@addOnFailureListener
            }
    }

    private fun setBottomSheetListeners() {
        binding.view.setOnClickListener(this)
        binding.save.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.view -> {
                navigation.postNavigation(Pair(NavigationDest.HOME, singleLocation))
            }

            binding.save -> {
                dataBaseViewModel.postDbAction(Pair(DbAction.SAVE, singleLocation))

            }
        }
        toggleActionView(false)

    }

    private fun toggleActionView(show: Boolean) {
        val transition: Transition = Fade()
        transition.duration = 600
        transition.addTarget(binding.actionsView)
        TransitionManager.beginDelayedTransition(binding.anchor, transition)
        binding.actionsView.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun noNetToast() {
        Toast(requireContext()).setNotificationToast(
            R.string.location_toast_error_title,
            R.string.location_out_of_box,
            R.color.color_danger,
            activity as FragmentActivity,
            Toast.LENGTH_SHORT
        )
    }

    override fun onMapLoaded() {
    }

}