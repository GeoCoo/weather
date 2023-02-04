package coo.apps.weather.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
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

