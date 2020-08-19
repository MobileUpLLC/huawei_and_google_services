package ru.kuchanov.huaweiandgoogleservices.ui.widget.map

import ru.kuchanov.huaweiandgoogleservices.domain.Location


abstract class SomeLatLngBounds(val southwest: Location? = null, val northeast: Location? = null) {

      abstract fun forLocations(locations: List<Location>): SomeLatLngBounds
}