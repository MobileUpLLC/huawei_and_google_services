package ru.kuchanov.huaweiandgoogleservices.domain

import ru.kuchanov.huaweiandgoogleservices.R
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker.SomeClusterItem

data class MarkerItem(
    val markerTitle: String,
    private val location: Location
) : SomeClusterItem {

    override fun getDrawableResourceId(selected: Boolean): Int {
        return if (selected) {
            R.drawable.ic_baseline_brightness_1_24
        } else {
            R.drawable.ic_baseline_adjust_24
        }
    }

    override fun getLocation() = location

    override fun getSnippet() = ""

    override fun getTitle() = markerTitle
}