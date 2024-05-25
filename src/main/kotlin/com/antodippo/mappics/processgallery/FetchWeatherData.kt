package com.antodippo.mappics.processgallery

import com.antodippo.mappics.gallery.WeatherData
import java.util.Date

interface FetchWeatherData {

    suspend fun fromGeoCoordinatesAndDatetime(latitude: Float, longitude: Float, datetime: Date): WeatherData
}

class UnableToFetchWeatherData(message: String) : Exception(message)