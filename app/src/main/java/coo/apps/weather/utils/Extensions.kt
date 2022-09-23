package coo.apps.weather.utils

import android.location.Location
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import coo.apps.weather.binders.abstraction.AdapterBinder
import coo.apps.weather.binders.abstraction.BaseItemViewBinder
import coo.apps.weather.binders.abstraction.ItemBinder
import coo.apps.weather.binders.abstraction.ItemClass
import coo.apps.weather.models.Limits
import coo.apps.weather.models.weather.WeatherResponse

fun Location.isInBoundBox(limits: Limits): Boolean {
    return limits.createBoundBox().contains(LatLng(this.latitude, this.longitude))

}

fun Limits.createBoundBox(): LatLngBounds {
    val builder = LatLngBounds.builder()
    builder.include(LatLng(this.xmin, this.xmax))
    builder.include(LatLng(this.ymin, this.ymax))
    return builder.build()
}

fun LifecycleOwner.getAdapterBinder(vararg binders: BaseItemViewBinder<out Any, *>): AdapterBinder {
    val viewBinders = mutableMapOf<ItemClass, ItemBinder>()

    for (b in binders) {
        b.registerLifecycle(lifecycle)
        @Suppress("UNCHECKED_CAST")
        viewBinders[b.modelClass] = b as ItemBinder
    }

    return AdapterBinder(viewBinders)
}


fun getAdapterBinder(vararg binders: BaseItemViewBinder<out Any, *>): AdapterBinder {
    val viewBinders = mutableMapOf<ItemClass, ItemBinder>()

    for (b in binders) {
        @Suppress("UNCHECKED_CAST")
        viewBinders[b.modelClass] = b as ItemBinder
    }

    return AdapterBinder(viewBinders)
}


fun WeatherResponse.convertToList(): List<WeatherResponse> {
    return listOf(this)
}
