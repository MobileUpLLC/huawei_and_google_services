package ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker

import com.google.android.gms.maps.model.Marker

class MarkerImpl(private val marker: Marker?) : SomeMarker() {
    override fun remove() {
        marker?.remove()
    }
}