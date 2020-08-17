package ru.kuchanov.huaweiandgoogleservices.system

import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Single

class PermissionsHelper {

    private var rxPermissions: RxPermissions? = null

    /**
     * Вызываем в Activity#onCreate
     */
    fun attach(activity: FragmentActivity) {
        rxPermissions = RxPermissions(activity)
    }

    /**
     * Вызываем в Activity#onDestroy
     */
    fun detach() {
        rxPermissions = null
    }

    fun requestPermission(vararg permissionName: String): Single<Boolean> {
        return rxPermissions?.request(*permissionName)
            ?.firstOrError()
            ?: Single.error(
                IllegalStateException("PermissionHelper is not attached to Activity")
            )
    }
}
