package ru.kuchanov.huaweiandgoogleservices.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject
import ru.kuchanov.huaweiandgoogleservices.R
import ru.kuchanov.huaweiandgoogleservices.analytics.Analytics
import ru.kuchanov.huaweiandgoogleservices.analytics.EventOpenMainScreen
import ru.kuchanov.huaweiandgoogleservices.analytics.EventOpenMapScreen
import ru.kuchanov.huaweiandgoogleservices.location.LocationGateway

class MainFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()

    private val analytics: Analytics by inject()
    private val locationGateway: LocationGateway by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView.setOnClickListener { analytics.send(EventOpenMainScreen()) }

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
                .addTo(compositeDisposable)
        }

        openMapButton.setOnClickListener {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.container, MapFragment(), MapFragment::class.java.simpleName)
                ?.addToBackStack(MapFragment::class.java.simpleName)
                ?.commit()

            analytics.send(EventOpenMapScreen())
        }
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }
}