package ru.kuchanov.huaweiandgoogleservices.domain

data class Location(
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        val DEFAULT_LOCATION = Location(55.744170, 37.627776)
    }
}

class UnknownLocationException : RuntimeException()