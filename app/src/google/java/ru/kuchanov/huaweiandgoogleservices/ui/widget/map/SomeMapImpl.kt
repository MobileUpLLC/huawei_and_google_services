package ru.kuchanov.huaweiandgoogleservices.ui.widget.map

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import ru.kuchanov.huaweiandgoogleservices.R
import ru.kuchanov.huaweiandgoogleservices.color
import ru.kuchanov.huaweiandgoogleservices.domain.Location
import ru.kuchanov.huaweiandgoogleservices.generateBitmapFromVectorResource
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker.*

class SomeMapImpl(val map: GoogleMap) : SomeMap() {

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
                    .position(
                        LatLng(
                            markerOptions.position.latitude,
                            markerOptions.position.longitude
                        )
                    )
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
        val clusterManager = ClusterManager<SomeClusterItemImpl<Item>>(context, map)
            .apply {
                setOnClusterItemClickListener {
                    clusterItemClickListener(it.someClusterItem)
                }

                setOnClusterClickListener { cluster ->
                    val position = Location(cluster.position.latitude, cluster.position.longitude)
                    val items: List<Item> = cluster.items.map { it.someClusterItem }
                    val someCluster: SomeCluster<Item> = SomeCluster(position, items)
                    clusterClickListener(someCluster)
                }
            }

        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)

        val renderer =
            object :
                DefaultClusterRenderer<SomeClusterItemImpl<Item>>(context, map, clusterManager),
                SelectableMarkerRenderer<SomeClusterItemImpl<Item>> {
                override val pinBitmapDescriptorsCache = mutableMapOf<Int, Bitmap>()

                override var selectedItem: SomeClusterItemImpl<Item>? = null

                override fun onBeforeClusterItemRendered(
                    item: SomeClusterItemImpl<Item>,
                    markerOptions: MarkerOptions
                ) {
                    val icon = generateClusterItemIconFun
                        ?.invoke(item.someClusterItem, item == selectedItem)
                        ?: getVectorResourceAsBitmap(
                            item.someClusterItem.getDrawableResourceId(item == selectedItem)
                        )
                    markerOptions
                        .icon(BitmapDescriptorFactory.fromBitmap(icon))
                        .zIndex(1.0f) // to hide cluster pin under the office pin
                }

                override fun getColor(clusterSize: Int): Int {
                    return context.resources.color(R.color.colorPrimary)
                }

                override fun selectItem(item: SomeClusterItemImpl<Item>?) {
                    selectedItem?.let {
                        val icon = generateClusterItemIconFun
                            ?.invoke(it.someClusterItem, false)
                            ?: getVectorResourceAsBitmap(
                                it.someClusterItem.getDrawableResourceId(false)
                            )
                        getMarker(it)?.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
                    }

                    selectedItem = item

                    item?.let {
                        val icon = generateClusterItemIconFun
                            ?.invoke(it.someClusterItem, true)
                            ?: getVectorResourceAsBitmap(
                                it.someClusterItem.getDrawableResourceId(true)
                            )
                        getMarker(it)?.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
                    }
                }

                override fun getVectorResourceAsBitmap(@DrawableRes vectorResourceId: Int): Bitmap {
                    return pinBitmapDescriptorsCache[vectorResourceId]
                        ?: context.resources.generateBitmapFromVectorResource(vectorResourceId)
                            .also { pinBitmapDescriptorsCache[vectorResourceId] = it }
                }
            }

        clusterManager.renderer = renderer

        clusterManager.clearItems()
        clusterManager.addItems(markers.map { SomeClusterItemImpl(it) })
        clusterManager.cluster()


        @Suppress("UnnecessaryVariable")
        val pinItemSelectedCallback = fun(item: Item?) {
            renderer.selectItem(item?.let { SomeClusterItemImpl(it) })
        }
        return pinItemSelectedCallback
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