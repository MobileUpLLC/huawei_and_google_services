package ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.TextView
import ru.kuchanov.huaweiandgoogleservices.R

/**
 * Not full copy of com.google.maps.android.ui.IconGenerator
 */
class IconGenerator(private val context: Context) {
    private val mContainer = LayoutInflater.from(context)
        .inflate(R.layout.map_marker_view, null as ViewGroup?) as ViewGroup
    private var mTextView: TextView?
    private var mContentView: View?

    init {
        mTextView = mContainer.findViewById(R.id.amu_text) as TextView
        mContentView = mTextView
    }

    fun makeIcon(text: CharSequence?): Bitmap {
        if (mTextView != null) {
            mTextView!!.text = text
        }
        return this.makeIcon()
    }

    fun makeIcon(): Bitmap {
        val measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        mContainer.measure(measureSpec, measureSpec)
        val measuredWidth = mContainer.measuredWidth
        val measuredHeight = mContainer.measuredHeight
        mContainer.layout(0, 0, measuredWidth, measuredHeight)
        val r = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        r.eraseColor(0)
        val canvas = Canvas(r)
        mContainer.draw(canvas)
        return r
    }

    fun setContentView(contentView: View?) {
        mContainer.removeAllViews()
        mContainer.addView(contentView)
        mContentView = contentView
        val view = mContainer.findViewById<View>(R.id.amu_text)
        mTextView = if (view is TextView) view else null
    }

    fun setBackground(background: Drawable?) {
        mContainer.setBackgroundDrawable(background)
        if (background != null) {
            val rect = Rect()
            background.getPadding(rect)
            mContainer.setPadding(rect.left, rect.top, rect.right, rect.bottom)
        } else {
            mContainer.setPadding(0, 0, 0, 0)
        }
    }

    fun setContentPadding(left: Int, top: Int, right: Int, bottom: Int) {
        mContentView!!.setPadding(left, top, right, bottom)
    }
}
