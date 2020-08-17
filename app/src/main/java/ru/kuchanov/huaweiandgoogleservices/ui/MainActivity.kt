package ru.kuchanov.huaweiandgoogleservices.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import ru.kuchanov.huaweiandgoogleservices.R
import ru.kuchanov.huaweiandgoogleservices.analytics.Analytics
import ru.kuchanov.huaweiandgoogleservices.analytics.EventOpenSomeScreen
import ru.kuchanov.huaweiandgoogleservices.location.LocationGateway
import ru.kuchanov.huaweiandgoogleservices.system.PermissionsHelper

class MainActivity : AppCompatActivity() {

    private val analytics: Analytics by inject()
    private val permissionHelper: PermissionsHelper by inject()
    private val locationGateway: LocationGateway by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionHelper.attach(this)

        analytics.send(EventOpenSomeScreen())

        textView.setOnClickListener { analytics.send(EventOpenSomeScreen()) }

        locationButton.setOnClickListener {
            locationGateway
                .requestLastLocation()
                .subscribeBy(
                    onSuccess = { Snackbar.make(root, "Location: $it", Snackbar.LENGTH_SHORT).show() },
                    onError = {
                        it.printStackTrace()
                        Snackbar.make(root, "Location error: ${it.message}", Snackbar.LENGTH_SHORT).show()
                    }
                )
        }
    }

    override fun onDestroy() {
        permissionHelper.detach()

        super.onDestroy()
    }
}