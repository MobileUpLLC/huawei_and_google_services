package ru.kuchanov.huaweiandgoogleservices.ui.widget.map

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import com.huawei.hms.maps.CameraUpdate
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.*
import ru.kuchanov.huaweiandgoogleservices.domain.Location
import ru.kuchanov.huaweiandgoogleservices.generateBitmapFromVectorResource
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker.*

class SomeMapImpl(val map: HuaweiMap) : SomeMap() {

    override fun setUiSettings(
        isMapToolbarEnabled: Boolean?,
        isCompassEnabled: Boolean?,
        isRotateGesturesEnabled: Boolean?,
        isMyLocationButtonEnabled: Boolean?,
        isZoomControlsEnabled: Boolean?
    ) {
        map.uiSettings.apply {
            isMapToolbarEnabled?.let {
                this.isMapToolbarEnabled = isMapToolbarEnabled
            }
            isCompassEnabled?.let {
                this.isCompassEnabled = isCompassEnabled
            }
            isRotateGesturesEnabled?.let {
                this.isRotateGesturesEnabled = isRotateGesturesEnabled
            }
            isMyLocationButtonEnabled?.let {
                this.isMyLocationButtonEnabled = isMyLocationButtonEnabled
            }
            isZoomControlsEnabled?.let {
                this.isZoomControlsEnabled = isZoomControlsEnabled
            }

            setAllGesturesEnabled(true)
        }
    }

    override fun animateCamera(someCameraUpdate: SomeCameraUpdate) {
        someCameraUpdate.toCameraUpdate()?.let { map.animateCamera(it) }
    }

    override fun moveCamera(someCameraUpdate: SomeCameraUpdate) {
        someCameraUpdate.toCameraUpdate()?.let { map.moveCamera(it) }
    }

    override fun setOnCameraIdleListener(function: () -> Unit) {
        map.setOnCameraIdleListener { function() }
    }

    override fun setOnMarkerClickListener(function: (SomeMarker) -> Boolean) {
        map.setOnMarkerClickListener { function(MarkerImpl(it)) }
    }

    override fun setOnMapClickListener(function: () -> Unit) {
        map.setOnMapClickListener { function() }
    }

    override fun setOnCameraMoveStartedListener(onCameraMoveStartedListener: (Int) -> Unit) {
        map.setOnCameraMoveStartedListener { onCameraMoveStartedListener(it) }
    }

    override fun addMarker(markerOptions: SomeMarkerOptions): SomeMarker {
        return MarkerImpl(
            map.addMarker(
                MarkerOptions()
                    .position(markerOptions.position.toLatLng())
                    .icon(BitmapDescriptorFactory.fromBitmap(markerOptions.icon))
            )
        )
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        map.setPadding(left, top, right, bottom)
    }

    override fun setOnCameraMoveListener(function: () -> Unit) {
        map.setOnCameraMoveListener { function() }
    }

    override fun <Item : SomeClusterItem> addMarkers(
        context: Context,
        markers: List<Item>,
        clusterItemClickListener: (Item) -> Boolean,
        clusterClickListener: (SomeCluster<Item>) -> Boolean,
        generateClusterItemIconFun: ((Item, Boolean) -> Bitmap)?
    ): (Item?) -> Unit {
        val addedMarkers = mutableListOf<Pair<Item, Marker>>()

        val selectableMarkerRenderer = object : SelectableMarkerRenderer<Item> {
            override val pinBitmapDescriptorsCache = mutableMapOf<Int, Bitmap>()

            override var selectedItem: Item? = null

            override fun selectItem(item: Item?) {
                selectedItem?.let {
                    val icon = generateClusterItemIconFun
                        ?.invoke(it, false)
                        ?: getVectorResourceAsBitmap(it.getDrawableResourceId(false))
                    getMarker(it)?.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
                }

                selectedItem = item

                item?.let {
                    val icon = generateClusterItemIconFun
                        ?.invoke(it, true)
                        ?: getVectorResourceAsBitmap(
                            it.getDrawableResourceId(true)
                        )
                    getMarker(it)?.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
                }
            }

            private fun getMarker(item: Item): Marker? {
                return addedMarkers.firstOrNull { it.first == item }?.second
            }

            override fun getVectorResourceAsBitmap(@DrawableRes vectorResourceId: Int): Bitmap {
                return pinBitmapDescriptorsCache[vectorResourceId]
                    ?: context.resources.generateBitmapFromVectorResource(vectorResourceId)
                        .also { pinBitmapDescriptorsCache[vectorResourceId] = it }
            }
        }

        addedMarkers += markers.map {
            val selected = selectableMarkerRenderer.selectedItem == it
            val icon = generateClusterItemIconFun
                ?.invoke(it, selected)
                ?: selectableMarkerRenderer.getVectorResourceAsBitmap(it.getDrawableResourceId(selected))

            val markerOptions = MarkerOptions()
                .position(it.getLocation().toLatLng())
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .clusterable(true)
            val marker = map.addMarker(markerOptions)

            it to marker
        }
        map.setMarkersClustering(true)

        map.setOnMarkerClickListener { clickedMarker ->
            val clickedItem = addedMarkers.firstOrNull { it.second == clickedMarker }?.first
            clickedItem?.let { clusterItemClickListener(it) } ?: false
        }

        return selectableMarkerRenderer::selectItem
    }
}

fun Location.toLatLng() = LatLng(latitude, longitude)

fun SomeLatLngBounds.toLatLngBounds() = LatLngBounds(southwest?.toLatLng(), northeast?.toLatLng())

fun SomeCameraUpdate.toCameraUpdate(): CameraUpdate? {
    return if (zoom != null) {
        CameraUpdateFactory.newCameraPosition(
            CameraPosition.fromLatLngZoom(
                location?.toLatLng()
                    ?: Location.DEFAULT_LOCATION.toLatLng(),
                zoom
            )
        )
    } else if (bounds != null && width != null && height != null && padding != null) {
        CameraUpdateFactory.newLatLngBounds(
            bounds.toLatLngBounds(),
            width,
            height,
            padding
        )
    } else {
        null
    }
}