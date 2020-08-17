package ru.kuchanov.huaweiandgoogleservices.location

import io.reactivex.Single
import ru.kuchanov.huaweiandgoogleservices.domain.Location

class LocationGateway(
    private val fusedLocationClient: FusedLocationClient
) {

    fun requestLastLocation(): Single<Location> {
        return fusedLocationClient.checkPermissions()
            .flatMap { granted ->
                if (granted) {
                    fusedLocationClient
                        .getLastLocation()
                        .onErrorResumeNext(fusedLocationClient.requestLastLocation())
                } else {
                    Single.just(Location.DEFAULT_LOCATION)
                }
            }
    }
}