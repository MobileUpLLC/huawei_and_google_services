package ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker

import com.huawei.hms.maps.model.Marker


class MarkerImpl(private val marker: Marker?) : SomeMarker() {
    override fun remove() {
        marker?.remove()
    }
}