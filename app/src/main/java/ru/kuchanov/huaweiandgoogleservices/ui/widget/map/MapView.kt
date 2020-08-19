package ru.kuchanov.huaweiandgoogleservices.ui.widget.map

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.FrameLayout
import ru.kuchanov.huaweiandgoogleservices.R

abstract class MapView : FrameLayout {

    enum class MapType(val value: Int) {
        NONE(0), NORMAL(1), SATELLITE(2), TERRAIN(3), HYBRID(4)
    }

    protected var mapType = MapType.NORMAL

    protected var liteModeEnabled = false

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet) {
        initAttributes(context, attrs)

        inflateMapViewImpl()
    }

    private fun initAttributes(context: Context, attrs: AttributeSet) {

        val attributeInfo = context.obtainStyledAttributes(
            attrs,
            R.styleable.MapView
        )

        mapType = MapType.values()[attributeInfo.getInt(
            R.styleable.MapView_someMapType,
            MapType.NORMAL.value
        )]

        liteModeEnabled = attributeInfo.getBoolean(R.styleable.MapView_liteModeEnabled, false)

        attributeInfo.recycle()
    }

    abstract fun inflateMapViewImpl()

    abstract fun onCreate(mapViewBundle: Bundle?)
    abstract fun onStart()
    abstract fun onResume()
    abstract fun onPause()
    abstract fun onStop()
    abstract fun onLowMemory()
    abstract fun onDestroy()

    abstract fun onSaveInstanceState(mapViewBundle: Bundle?)

    abstract fun getMapAsync(function: (SomeMap) -> Unit)
}
