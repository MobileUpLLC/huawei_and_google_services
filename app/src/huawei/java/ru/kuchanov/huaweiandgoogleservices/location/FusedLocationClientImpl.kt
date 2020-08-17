package ru.kuchanov.huaweiandgoogleservices.location

import android.Manifest
import android.content.Context
import android.os.Build
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationResult
import com.huawei.hms.location.LocationServices
import io.reactivex.Single
import ru.kuchanov.huaweiandgoogleservices.domain.Location
import ru.kuchanov.huaweiandgoogleservices.domain.UnknownLocationException
import ru.kuchanov.huaweiandgoogleservices.system.PermissionsHelper

class FusedLocationClientImpl(
    private val permissionsHelper: PermissionsHelper,
    context: Context
) : FusedLocationClient {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun checkPermissions(): Single<Boolean> {
        val permissions = mutableListOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            // for huawei we need this permission too after API=28
            permissions += Manifest.permission.ACCESS_BACKGROUND_LOCATION
        }
        return permissionsHelper.requestPermission(*permissions.toTypedArray())
    }

    override fun getLastLocation(): Single<Location> {
        return Single.create { singleEmitter ->
            fusedLocationClient.lastLocation
                .addOnFailureListener {
                    if (singleEmitter.isDisposed) return@addOnFailureListener

                    singleEmitter.onError(it)
                }
                .addOnSuccessListener { newLocation ->
                    if (singleEmitter.isDisposed) return@addOnSuccessListener

                    if (newLocation == null) {
                        singleEmitter.onError(UnknownLocationException())
                    } else {
                        singleEmitter.onSuccess(
                            Location(
                                newLocation.latitude,
                                newLocation.longitude
                            )
                        )
                    }
                }
        }
    }

    override fun requestLastLocation(): Single<Location> {
        return Single.create { singleEmitter ->

            val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setSmallestDisplacement(5.5F)
                .setNumUpdates(1)

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    if (singleEmitter.isDisposed) return

                    singleEmitter.onSuccess(
                        Location(
                            result.lastLocation.latitude,
                            result.lastLocation.longitude
                        )
                    )
                }
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, callback, null)

            singleEmitter.setCancellable {
                fusedLocationClient.removeLocationUpdates(callback)
            }
        }
    }
}