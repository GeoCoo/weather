package coo.apps.meteoray.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import java.text.SimpleDateFormat

fun getPlaceNameFromLocation(context: Context, lat: Double?, long: Double?): Address? {
    val geocoder = Geocoder(context)
    val addresses = geocoder.getFromLocation(lat!!, long!!, 1)
    return addresses?.get(0)
}

fun convertDate(date: String?): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy")
    val finalDate = formatter.parse(date.toString())
    return SimpleDateFormat("EEE d").format(finalDate!!)
}


fun createLocation(lat: Double, lon: Double): Location {
    val location = Location(LocationManager.GPS_PROVIDER)
    location.latitude = lat
    location.longitude = lon
    return location
}
