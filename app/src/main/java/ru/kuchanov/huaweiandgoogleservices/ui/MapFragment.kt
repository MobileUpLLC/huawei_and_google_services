package ru.kuchanov.huaweiandgoogleservices.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.android.ext.android.inject
import ru.kuchanov.huaweiandgoogleservices.R
import ru.kuchanov.huaweiandgoogleservices.api.MapMarkersGateway
import ru.kuchanov.huaweiandgoogleservices.domain.Location
import ru.kuchanov.huaweiandgoogleservices.domain.MarkerItem
import ru.kuchanov.huaweiandgoogleservices.dp
import ru.kuchanov.huaweiandgoogleservices.generateBitmapFromVectorResource
import ru.kuchanov.huaweiandgoogleservices.location.LocationGateway
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.SomeCameraUpdate
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.SomeLatLngBoundsImpl
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.SomeMap
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker.SomeMarker
import ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker.SomeMarkerOptions

class MapFragment : Fragment() {

    companion object {
        private const val MAP_VIEW_BUNDLE_KEY = "map_view_bundle_key"
        private const val DEFAULT_ZOOM = 11.0f
    }

    private val compositeDisposable = CompositeDisposable()

    private val locationGateway: LocationGateway by inject()

    private val mapMaGateway: MapMarkersGateway by inject()

    private var deviceLocationMarker: SomeMarker? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        // https://github.com/googlemaps/android-samples/blob/master/ApiDemos/java/app/src/main/java/com/example/mapdemo/RawMapViewDemoActivity.java
        val mapViewBundle = savedInstanceState?.getBundle(MAP_VIEW_BUNDLE_KEY)

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync { onMapReady(it) }
    }

    private fun onMapReady(map: SomeMap) {
        map.setUiSettings(isMapToolbarEnabled = false, isCompassEnabled = false)

        locationButton.setOnClickListener {
            locationGateway.requestLastLocation()
                .subscribeBy(
                    onSuccess = { bindDeviceLocation(map, it) },
                    onError = {
                        it.printStackTrace()
                        Snackbar.make(root, "Location error: ${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                )
                .addTo(compositeDisposable)
        }

        locationGateway.requestLastLocation()
            .subscribeBy(
                onSuccess = { bindDeviceLocation(map, it) },
                onError = {
                    it.printStackTrace()
                    Snackbar.make(root, "Location error: ${it.message}", Snackbar.LENGTH_SHORT).show()
                }
            )
            .addTo(compositeDisposable)

        var pinItemSelected: ((MarkerItem?) -> Unit)? = null

        fun onMarkerSelected(selectedMarkerItem: MarkerItem?) {
            pinItemSelected?.invoke(selectedMarkerItem)
            selectedMarkerItem?.let {
                map.animateCamera(SomeCameraUpdate(it.getLocation(), DEFAULT_ZOOM))
                Snackbar.make(root, "Marker selected: ${it.markerTitle}", Snackbar.LENGTH_SHORT).show()
            }
        }

        with(map) {
            setOnMapClickListener {
                onMarkerSelected(null)
            }

            setOnCameraMoveStartedListener { reason ->
                if (reason == SomeMap.REASON_GESTURE) {
                    onMarkerSelected(null)
                }
            }
        }

        locationGateway.requestLastLocation()
            .flatMap { mapMaGateway.getMapMarkers(it) }
            .subscribeBy { officeItemList ->
                pinItemSelected = map.addMarkers(
                    requireContext(),
                    officeItemList.map { it },
                    {
                        onMarkerSelected(it)
                        true
                    },
                    { someCluster ->
                        mapView?.let { mapViewRef ->
                            val bounds = SomeLatLngBoundsImpl()
                                .forLocations(someCluster.items.map { it.getLocation() })

                            val someCameraUpdate = SomeCameraUpdate(
                                bounds = bounds,
                                width = mapViewRef.width,
                                height = mapViewRef.height,
                                padding = 32.dp()
                            )

                            map.animateCamera(someCameraUpdate)
                        }

                        onMarkerSelected(null)

                        true
                    }
                )
            }
            .addTo(compositeDisposable)
    }

    private fun bindDeviceLocation(map: SomeMap, location: Location?) {
        map.animateCamera(
            SomeCameraUpdate(location ?: Location.DEFAULT_LOCATION, DEFAULT_ZOOM)
        )

        deviceLocationMarker?.remove()
        deviceLocationMarker = location?.let {
            map.addMarker(
                SomeMarkerOptions(
                    resources.generateBitmapFromVectorResource(R.drawable.ic_baseline_face_24),
                    it
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        view?.requestApplyInsets()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveMapInstanceState(outState)
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        deviceLocationMarker?.remove()
        deviceLocationMarker = null

        mapView.onDestroy()
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun saveMapInstanceState(outState: Bundle?) {
        val mapViewBundle = Bundle()
        mapView.onSaveInstanceState(mapViewBundle)
        outState?.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
    }
}