package ru.kuchanov.huaweiandgoogleservices.ui.widget.map

import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.LatLngBounds
import ru.kuchanov.huaweiandgoogleservices.domain.Location

class SomeLatLngBoundsImpl(bounds: LatLngBounds? = null) :
    SomeLatLngBounds(bounds?.southwest?.toLocation(), bounds?.northeast?.toLocation()) {

    override fun forLocations(locations: List<Location>): SomeLatLngBounds {
        val bounds = LatLngBounds.builder()
            .apply { locations.map { it.toLatLng() }.forEach { include(it) } }
            .build()

        return SomeLatLngBoundsImpl(bounds)
    }
}

fun LatLng.toLocation(): Location {
    return Location(latitude, longitude)
}