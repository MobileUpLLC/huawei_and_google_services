package ru.kuchanov.huaweiandgoogleservices.ui.widget.map

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.gms.maps.GoogleMapOptions

class MapViewImpl : MapView {

    private lateinit var mapView: com.google.android.gms.maps.MapView

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun inflateMapViewImpl() {
        mapView = com.google.android.gms.maps.MapView(
            context,
            GoogleMapOptions().liteMode(liteModeEnabled).mapType(mapType.value)
        )

        addView(mapView)
    }

    override fun getMapAsync(function: (SomeMap) -> Unit) {
        mapView.getMapAsync { function(SomeMapImpl(it)) }
    }

    override fun onCreate(mapViewBundle: Bundle?) {
        mapView.onCreate(mapViewBundle)
    }

    override fun onStart() {
        mapView.onStart()
    }

    override fun onResume() {
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
    }

    override fun onStop() {
        mapView.onStop()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(mapViewBundle: Bundle?) {
        mapView.onSaveInstanceState(mapViewBundle)
    }

    /**
     * We need to manually pass touch events to MapView
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mapView.onTouchEvent(event)
        return true
    }

    /**
     * We need to manually pass touch events to MapView
     */
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        mapView.dispatchTouchEvent(event)
        return true
    }
}
