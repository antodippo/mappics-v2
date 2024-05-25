package com.antodippo.mappics.processgallery

interface FetchLocationDescription {

    suspend fun fromGeoCoordinates(latitude: Float, longitude: Float): LocationDescription
}

data class LocationDescription(
    val shortDescription: String,
    val longDescription: String,
)

class UnableToFetchLocationDescription(message: String) : Exception(message)