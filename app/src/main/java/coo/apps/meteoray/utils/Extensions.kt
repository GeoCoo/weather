package coo.apps.meteoray.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolygonOptions
import coo.apps.meteoray.R
import coo.apps.meteoray.activities.MainActivity
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

fun Limits?.createBoundBox(): LatLngBounds {
    val limits = this
    val builder = LatLngBounds.builder()
    if (limits != null) {
        builder.include(LatLng(limits.latmin, limits.lonmin))
        builder.include(LatLng(limits.latmax, limits.lonmax))
    } else {
        builder.include(LatLng(39.55, 21.65))
        builder.include(LatLng(41.15, 21.65))
    }
    return builder.build()
}

fun LatLngBounds.createRect(color: Int): PolygonOptions {
    return PolygonOptions()
        .add(LatLng(this.northeast.latitude, this.northeast.longitude))
        .add(LatLng(this.southwest.latitude, this.northeast.longitude))
        .add(LatLng(this.southwest.latitude, this.southwest.longitude))
        .add(LatLng(this.northeast.latitude, this.southwest.longitude)).strokeColor(color)
}


fun Location?.getPlaceNameFromLocation(context: Context): Address? {
    val geocoder = Geocoder(context)
    val addresses = this?.latitude?.let { geocoder.getFromLocation(it, this.longitude, 1) }
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

fun Toast.setNotificationToast(
    title: Int, message: Int, color: Int, activity: AppCompatActivity, durationToast: Int
) {
    if (activity is MainActivity) {
        val layout = activity.layoutInflater.inflate(
            R.layout.notification_toast, activity.findViewById(R.id.toastContainer)
        )

        val toastText = layout.findViewById<TextView>(R.id.toastText)
        val toastCard = layout.findViewById<CardView>(R.id.toastCard)
        val toastTitle = layout.findViewById<TextView>(R.id.toastTitle)
        toastText.text = activity.resources.getString(message)
        toastCard.setCardBackgroundColor(activity.resources.getColor(color))
        toastTitle.text = activity.resources.getString(title)

        this.apply {
            setGravity(Gravity.BOTTOM, 0, 40)
            duration = durationToast
            view = layout
            show()
        }
    }
}
