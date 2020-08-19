package ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import ru.kuchanov.huaweiandgoogleservices.domain.Location
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.toLatLng

data class SomeClusterItemImpl<T : SomeClusterItem>(
    val someClusterItem: T
) : ClusterItem, SomeClusterItem {

    override fun getSnippet(): String {
        return someClusterItem.getSnippet() ?: ""
    }

    override fun getTitle(): String {
        return someClusterItem.getTitle() ?: ""
    }

    override fun getPosition(): LatLng {
        return someClusterItem.getLocation().toLatLng()
    }

    override fun getLocation(): Location {
        return someClusterItem.getLocation()
    }
}