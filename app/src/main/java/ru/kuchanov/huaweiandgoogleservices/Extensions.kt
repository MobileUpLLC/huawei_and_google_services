package ru.kuchanov.huaweiandgoogleservices

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat

@ColorInt
fun Resources.color(@ColorRes colorId: Int) = ResourcesCompat.getColor(this, colorId, null)

fun Resources.generateBitmapFromVectorResource(@DrawableRes vectorResourceId: Int): Bitmap {
    return getDrawable(vectorResourceId, null)!!
        .apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
        .let { drawable ->
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            ).apply {
                drawable.draw(Canvas(this))
            }
        }
}

fun Int.dp() = (this * Resources.getSystem().displayMetrics.density).toInt()