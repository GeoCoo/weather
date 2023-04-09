package coo.apps.meteoray.utils

import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolygonOptions
import coo.apps.meteoray.locationsDb.LocationEntity
import coo.apps.meteoray.models.Limits
import java.text.SimpleDateFormat


fun Location.isInBoundBox(limits: Limits?): Boolean? {
    return limits?.createBoundBox()?.contains(LatLng(this.latitude, this.longitude))
}

fun LatLng.checkIfIsInBox(limits: Limits?): Boolean? {
    val location = this.createLocation()
    return location.isInBoundBox(limits)
}

fun Limits.createBoundBox(): LatLngBounds {
    val builder = LatLngBounds.builder()
    builder.include(LatLng(this.latmin, this.lonmin))
    builder.include(LatLng(this.latmax, this.lonmax))
    return builder.build()
}

fun LatLngBounds.createRect(color: Int): PolygonOptions {
    return PolygonOptions()
        .add(LatLng(this.northeast.latitude, this.northeast.longitude))
        .add(LatLng(this.southwest.latitude, this.northeast.longitude))
        .add(LatLng(this.southwest.latitude, this.southwest.longitude))
        .add(LatLng(this.northeast.latitude, this.southwest.longitude)).strokeColor(color)
}


suspend fun Location.getPlaceNameFromLocation(context: Context): Address? {
    val geocoder = Geocoder(context)
    val addresses = geocoder.getFromLocation(this.latitude, this.longitude, 1)
    return addresses?.get(0)
}

fun String.convertDate(): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy")
    val finalDate = formatter.parse(this)
    return SimpleDateFormat("EEE d").format(finalDate!!)
}


fun LatLng.createLocation(): Location {
    val location = Location(LocationManager.GPS_PROVIDER)
    location.latitude = this.latitude
    location.longitude = this.longitude
    return location
}


fun LocationEntity.createLocation(): Location {
    val location = Location(LocationManager.GPS_PROVIDER)
    location.latitude = this.locationLat
    location.longitude = this.locationLon
    return location
}

fun Activity.dismissKeyboard() {
    val imm: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (null != this.currentFocus) imm.hideSoftInputFromWindow(
        this.currentFocus!!
            .applicationWindowToken, 0
    )
}


fun Activity.openKeyboard(ediText: EditText) {
    val imm: InputMethodManager? =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(ediText, InputMethodManager.SHOW_IMPLICIT)
}