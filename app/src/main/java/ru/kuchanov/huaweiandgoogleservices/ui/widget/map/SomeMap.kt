package ru.kuchanov.huaweiandgoogleservices.ui.widget.map

import android.content.Context
import android.graphics.Bitmap
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker.SomeCluster
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker.SomeClusterItem
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker.SomeMarker
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker.SomeMarkerOptions


abstract class SomeMap {

    abstract fun setUiSettings(
        isMapToolbarEnabled: Boolean? = null,
        isCompassEnabled: Boolean? = null,
        isRotateGesturesEnabled: Boolean? = null,
        isMyLocationButtonEnabled: Boolean? = null,
        isZoomControlsEnabled: Boolean? = null
    )

    abstract fun animateCamera(someCameraUpdate: SomeCameraUpdate)
    abstract fun moveCamera(someCameraUpdate: SomeCameraUpdate)
    abstract fun setOnCameraIdleListener(function: () -> Unit)
    abstract fun setOnMarkerClickListener(function: (SomeMarker) -> Boolean)
    abstract fun setOnMapClickListener(function: () -> Unit)

    abstract fun addMarker(markerOptions: SomeMarkerOptions): SomeMarker

    abstract fun <Item : SomeClusterItem> addMarkers(
        context: Context,
        markers: List<Item>,
        clusterItemClickListener: (Item) -> Boolean,
        clusterClickListener: (SomeCluster<Item>) -> Boolean,
        generateClusterItemIconFun: ((Item, Boolean) -> Bitmap)? = null
    ): (Item?) -> Unit

    abstract fun setPadding(left: Int, top: Int, right: Int, bottom: Int)
    abstract fun setOnCameraMoveStartedListener(onCameraMoveStartedListener: (Int) -> Unit)
    abstract fun setOnCameraMoveListener(function: () -> Unit)

    companion object {
        const val REASON_GESTURE = 1
        const val REASON_API_ANIMATION = 2
        const val REASON_DEVELOPER_ANIMATION = 3
    }
}