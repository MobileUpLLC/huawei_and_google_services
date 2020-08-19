package ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker

import android.graphics.Bitmap
import ru.kuchanov.huaweiandgoogleservices.domain.Location

data class SomeMarkerOptions(
    val icon: Bitmap,
    val position: Location
)