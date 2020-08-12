package ru.kuchanov.huaweiandgoogleservices.di

import org.koin.dsl.module
import ru.kuchanov.huaweiandgoogleservices.analytics.Analytics
import ru.kuchanov.huaweiandgoogleservices.analytics.AnalyticsImpl

object AppModule {

    fun create() = module {
        single<Analytics> { AnalyticsImpl(get()) }
    }
}