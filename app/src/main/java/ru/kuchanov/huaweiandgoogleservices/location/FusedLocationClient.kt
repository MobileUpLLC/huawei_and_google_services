package ru.kuchanov.huaweiandgoogleservices.location

import io.reactivex.Single
import ru.kuchanov.huaweiandgoogleservices.domain.Location

interface FusedLocationClient {

    fun checkPermissions(): Single<Boolean>

    fun getLastLocation(): Single<Location>

    fun requestLastLocation(): Single<Location>
}