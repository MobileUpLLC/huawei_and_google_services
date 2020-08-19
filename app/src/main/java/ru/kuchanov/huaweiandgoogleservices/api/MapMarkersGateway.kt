package ru.kuchanov.huaweiandgoogleservices.api

import io.reactivex.Single
import ru.kuchanov.huaweiandgoogleservices.domain.Location
import ru.kuchanov.huaweiandgoogleservices.domain.MarkerItem
import kotlin.random.Random

class MapMarkersGateway {

    fun getMapMarkers(location: Location): Single<List<MarkerItem>> {
        return Single.fromCallable {
            val random = Random.Default
            (1..10).map {
                MarkerItem(
                    it.toString(),
                    Location(
                        location.latitude -.1 + random.nextDouble(.2),
                        location.longitude -.1 + random.nextDouble(.2)
                    )
                )
            }
        }
    }
}