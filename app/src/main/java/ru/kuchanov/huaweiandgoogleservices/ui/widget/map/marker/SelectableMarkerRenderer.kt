package ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker

import android.graphics.Bitmap
import androidx.annotation.DrawableRes

interface SelectableMarkerRenderer<Item : SomeClusterItem> {
    val pinBitmapDescriptorsCache: Map<Int, Bitmap>

    var selectedItem: Item?

    fun selectItem(item: Item?)

    fun getVectorResourceAsBitmap(@DrawableRes vectorResourceId: Int): Bitmap
}