package ru.kuchanov.huaweiandgoogleservices.di

import org.koin.dsl.module
import ru.kuchanov.huaweiandgoogleservices.system.PermissionsHelper

object SystemModule {

    fun create() = module {
        single { PermissionsHelper() }
    }
}