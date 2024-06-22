package com.antodippo.mappics.processgallery

import com.antodippo.mappics.gallery.WeatherData
import com.antodippo.mappics.http.HTTPClient
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

@Service
class FetchWeatherDataFromVisualCrossing(private val client: HTTPClient): FetchWeatherData {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    override suspend fun fromGeoCoordinatesAndDatetime(latitude: Float, longitude: Float, datetime: Date): WeatherData {

        val datetimeString = dateFormat.format(datetime)
        val apiKey = System.getProperty("VISUALCROSSING_API_KEY")
        val response = client.get(URI("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/$latitude,$longitude/$datetimeString?key=$apiKey&include=current&unitGroup=metric"))

        if (response.statusCode() != 200) {
            throw UnableToFetchWeatherData("Unable to fetch weather data - lat: $latitude - lon: $longitude - datetime: $datetimeString - message: ${response.body()}")
        }

        val jsonBody = jacksonObjectMapper().readValue<JsonNode>(response.body())
        val day = jsonBody["days"].first()

        return WeatherData(
            description = day["conditions"].asText(),
            temperature = day["temp"].asDouble().toFloat(),
            humidity = day["humidity"].asDouble().toFloat(),
            pressure = day["pressure"].asDouble().toFloat(),
            windSpeed = day["windspeed"].asDouble().toFloat(),
        )
    }
}