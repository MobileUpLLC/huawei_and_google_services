package ru.kuchanov.huaweiandgoogleservices.ui.widget.map

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import ru.kuchanov.huaweiandgoogleservices.domain.Location


class SomeLatLngBoundsImpl(bounds: LatLngBounds? = null) :
    SomeLatLngBounds(bounds?.southwest?.toLocation(), bounds?.northeast?.toLocation()) {

    override fun forLocations(locations: List<Location>): SomeLatLngBounds {
        val bounds = LatLngBounds.builder()
            .apply {
                locations.map { LatLng(it.latitude, it.longitude) }.forEach { include(it) }
            }
            .build()

        return SomeLatLngBoundsImpl(bounds)
    }
}

fun LatLng.toLocation(): Location {
    return Location(latitude, longitude)
}