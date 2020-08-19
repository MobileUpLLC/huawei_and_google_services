package ru.kuchanov.huaweiandgoogleservices.ui.widget.map

import ru.kuchanov.huaweiandgoogleservices.domain.Location


class SomeCameraUpdate private constructor(
    val location: Location? = null,
    val zoom: Float? = null,
    val bounds: SomeLatLngBounds? = null,
    val width: Int? = null,
    val height: Int? = null,
    val padding: Int? = null
) {
    constructor(
        location: Location? = null,
        zoom: Float? = null
    ) : this(location, zoom, null, null, null, null)

    constructor(
        bounds: SomeLatLngBounds? = null,
        width: Int? = null,
        height: Int? = null,
        padding: Int? = null
    ) : this(null, null, bounds, width, height, padding)
}