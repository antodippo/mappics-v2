package com.antodippo.mappics.gallery

import java.util.*

class Picture(
    val filename: String,
    val exifData: ExifData,
) {
    val id: String = UUID.randomUUID().toString()
    val createdAt: Date = Date()
    var resizedFilename: String = ""
    var thumbnailFilename: String = ""
    var description: String = ""
    var longDescription: String = ""
    var weather: WeatherData = WeatherData(
        description = "",
        temperature = 0.0f,
        humidity = 0.0f,
        pressure = 0.0f,
        windSpeed = 0.0f,
    )

    // No argument constructor for Firestore
    constructor(): this(
        "",
        ExifData(
            make = "",
            model = "",
            exposure = "",
            aperture = "",
            iso = "",
            focalLength = "",
            gpsLatitude = 0.0f,
            gpsLongitude = 0.0f,
            gpsAltitude = 0.0f,
            takenAt = Date()
        )
    )
}

data class WeatherData (
    val description: String,
    val temperature: Float,
    val humidity: Float,
    val pressure: Float,
    val windSpeed: Float,
) {

    // No argument constructor for Firestore
    constructor(): this(
        description = "",
        temperature = 0.0f,
        humidity = 0.0f,
        pressure = 0.0f,
        windSpeed = 0.0f
    )

    init {
        if (humidity < 0 || humidity > 100) {
            throw IllegalArgumentException("Humidity must be between 0 and 100")
        }
    }
}

data class ExifData (
    val make: String,
    val model: String,
    val exposure: String,
    val aperture: String,
    val iso: String,
    val focalLength: String,
    val gpsLatitude: Float,
    val gpsLongitude: Float,
    val gpsAltitude: Float,
    val takenAt: Date,
) {

    // No argument constructor for Firestore
    constructor(): this(
        make = "",
        model = "",
        exposure = "",
        aperture = "",
        iso = "",
        focalLength = "",
        gpsLatitude = 0.0f,
        gpsLongitude = 0.0f,
        gpsAltitude = 0.0f,
        takenAt = Date()
    )

    init {
        if (gpsLatitude < -90 || gpsLatitude > 90) {
            throw IllegalArgumentException("Latitude must be between -90 and 90")
        }
        if (gpsLongitude < -180 || gpsLongitude > 180) {
            throw IllegalArgumentException("Longitude must be between -180 and 180")
        }
        if (gpsAltitude < 0) {
            throw IllegalArgumentException("Altitude must be greater than 0")
        }
    }
}
