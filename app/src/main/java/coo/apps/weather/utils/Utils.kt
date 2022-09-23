package coo.apps.weather.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import coo.apps.weather.binders.abstraction.AdapterBinder
import coo.apps.weather.binders.abstraction.BaseItemViewBinder
import coo.apps.weather.binders.abstraction.ItemBinder
import coo.apps.weather.binders.abstraction.ItemClass
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

