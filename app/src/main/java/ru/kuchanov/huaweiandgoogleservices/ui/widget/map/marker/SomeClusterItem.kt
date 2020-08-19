package ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker

import ru.kuchanov.huaweiandgoogleservices.domain.Location


interface SomeClusterItem {
    fun getLocation(): Location

    fun getTitle(): String?

    fun getSnippet(): String?

    fun getDrawableResourceId(selected: Boolean): Int {
        return 0
    }
}