package ru.kuchanov.huaweiandgoogleservices

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.kuchanov.huaweiandgoogleservices.di.AppModule
import ru.kuchanov.huaweiandgoogleservices.di.SystemModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(AppModule.create(), SystemModule.create()))
        }
    }
}