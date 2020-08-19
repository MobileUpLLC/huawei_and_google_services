package ru.kuchanov.huaweiandgoogleservices.di

import org.koin.dsl.module
import ru.kuchanov.huaweiandgoogleservices.analytics.Analytics
import ru.kuchanov.huaweiandgoogleservices.analytics.AnalyticsImpl
import ru.kuchanov.huaweiandgoogleservices.api.MapMarkersGateway
import ru.kuchanov.huaweiandgoogleservices.location.FusedLocationClient
import ru.kuchanov.huaweiandgoogleservices.location.FusedLocationClientImpl
import ru.kuchanov.huaweiandgoogleservices.location.LocationGateway

object AppModule {

    fun create() = module {
        single<Analytics> { AnalyticsImpl(get()) }
        single<FusedLocationClient> { FusedLocationClientImpl(get(), get()) }
        single { LocationGateway(get()) }
        single { MapMarkersGateway() }
    }
}