package com.antodippo.mappics.processgallery

import com.antodippo.mappics.gallery.WeatherData
import com.antodippo.mappics.http.HTTPClientWithJavaHttpClient
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.text.SimpleDateFormat
import java.util.*


@Disabled("This test makes an HTTP call, it should be run locally and not in the CI pipeline.")
class FetchWeatherDataFromVisualCrossingTest {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    @Test
    //TODO write a unit test
    fun `It returns weather data for valid geo coordinates and datetime`() = runBlocking {

        val fetchWeatherData = FetchWeatherDataFromVisualCrossing(HTTPClientWithJavaHttpClient())
        val weatherData = fetchWeatherData.fromGeoCoordinatesAndDatetime(
            latitude = 57.839184f,
            longitude = 25.793507f,
            datetime = dateFormat.parse("2018-08-16 16:01:00")
        )

        assertEquals(
            WeatherData(
                description = "Rain, Partially cloudy",
                temperature = 18.1f,
                humidity = 82.9f,
                pressure = 1017.8f,
                windSpeed = 7.1f,
            ),
            weatherData
        )
    }

    @Test
    fun `It throws an exception when unable to geo locate`(): Unit = runBlocking {

        val fetchWeatherData = FetchWeatherDataFromVisualCrossing(HTTPClientWithJavaHttpClient())

        assertThrows<UnableToFetchWeatherData> {
            fetchWeatherData.fromGeoCoordinatesAndDatetime(
                latitude = 157.83f,
                longitude = 225.79f,
                datetime = dateFormat.parse("2018-08-16 16:01:00")
            )
        }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup(): Unit {
            dotenv().entries().map { System.setProperty(it.key, it.value) }
        }
    }
}