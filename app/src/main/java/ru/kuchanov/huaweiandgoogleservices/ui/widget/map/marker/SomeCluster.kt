package ru.kuchanov.huaweiandgoogleservices.ui.widget.map.marker

import ru.kuchanov.huaweiandgoogleservices.domain.Location


data class SomeCluster<T : SomeClusterItem>(
    val location: Location,
    val items: List<T>
)