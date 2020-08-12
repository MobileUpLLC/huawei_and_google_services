package ru.kuchanov.huaweiandgoogleservices.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import ru.kuchanov.huaweiandgoogleservices.R
import ru.kuchanov.huaweiandgoogleservices.analytics.Analytics
import ru.kuchanov.huaweiandgoogleservices.analytics.EventOpenSomeScreen

class MainActivity : AppCompatActivity() {

    val analytics: Analytics by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        analytics.send(EventOpenSomeScreen())

        textView.setOnClickListener { analytics.send(EventOpenSomeScreen()) }
    }
}