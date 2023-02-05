package coo.apps.weather.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolygonOptions
import coo.apps.weather.models.Limits

fun Location.isInBoundBox(limits: Limits): Boolean {
    return limits.createBoundBox().contains(LatLng(this.latitude, this.longitude))

}

fun Limits.createBoundBox(): LatLngBounds {
    val builder = LatLngBounds.builder()
    builder.include(LatLng(this.xmin, this.xmax))
    builder.include(LatLng(this.ymin, this.ymax))
    return builder.build()
}

fun LatLngBounds.createRect(color: Int): PolygonOptions {
    return PolygonOptions().add(LatLng(this.northeast.latitude, this.northeast.longitude))
        .add(LatLng(this.southwest.latitude, this.northeast.longitude))
        .add(LatLng(this.southwest.latitude, this.southwest.longitude))
        .add(LatLng(this.northeast.latitude, this.southwest.longitude)).strokeColor(color)
}