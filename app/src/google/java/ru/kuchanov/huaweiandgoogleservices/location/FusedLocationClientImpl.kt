package ru.kuchanov.huaweiandgoogleservices.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
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
        return permissionsHelper.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @SuppressLint("MissingPermission")
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

    @SuppressLint("MissingPermission")
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