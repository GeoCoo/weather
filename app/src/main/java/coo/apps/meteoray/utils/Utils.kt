package coo.apps.meteoray.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import coo.apps.meteoray.locationsDb.LocationEntity
import java.text.SimpleDateFormat

fun Location.getPlaceNameFromLocation(context: Context): Address? {
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