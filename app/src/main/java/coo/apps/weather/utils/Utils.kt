package coo.apps.weather.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import coo.apps.weather.models.Limits
import java.text.SimpleDateFormat

fun getPlaceNameFromLocation(context: Context, lat: Double?, long: Double?): Address? {
    val geocoder = Geocoder(context)
    val addresses = geocoder.getFromLocation(lat!!, long!!, 1)
    return addresses[0]
}

fun convertDate(date: String?): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy")
    val finalDate = formatter.parse(date.toString())
    return SimpleDateFormat("EEE d").format(finalDate!!)
}


fun Location.handleBoundBox(limits: Limits): Boolean {
    return limits.createBoundBox().contains(LatLng(this.latitude, this.longitude))

}

fun Limits.createBoundBox(): LatLngBounds {
    val builder = LatLngBounds.builder()
    builder.include(LatLng(this.xmin, this.xmax))
    builder.include(LatLng(this.ymin, this.ymax))
    return builder.build()

}