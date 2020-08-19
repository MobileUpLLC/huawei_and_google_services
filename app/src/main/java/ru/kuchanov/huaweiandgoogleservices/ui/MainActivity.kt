package ru.kuchanov.huaweiandgoogleservices.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import ru.kuchanov.huaweiandgoogleservices.R
import ru.kuchanov.huaweiandgoogleservices.system.PermissionsHelper

class MainActivity : AppCompatActivity() {

    private val permissionHelper: PermissionsHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionHelper.attach(this)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, MainFragment(), MainFragment::class.java.simpleName)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        permissionHelper.detach()

        super.onDestroy()
    }
}