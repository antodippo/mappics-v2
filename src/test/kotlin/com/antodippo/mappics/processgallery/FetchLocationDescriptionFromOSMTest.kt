package com.antodippo.mappics.processgallery

import com.antodippo.mappics.http.HTTPClientWithJavaHttpClient
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@Disabled("This test makes an HTTP call, it should be run locally and not in the CI pipeline.")
class FetchLocationDescriptionFromOSMTest {

    @ParameterizedTest
    @MethodSource("provideLocationCoordinatesAndExpectedLocationDescription")
    //TODO write a unit test
    fun `It returns location description for valid geo coordinates`(
        latitude: Float,
        longitude: Float,
        expectedLocationDescription: LocationDescription,
        ) = runBlocking {

        val fetchLocationDescription = FetchLocationDescriptionFromOSM(HTTPClientWithJavaHttpClient())
        val locationDescription = fetchLocationDescription.fromGeoCoordinates(latitude, longitude)

        assertEquals(locationDescription, expectedLocationDescription)
    }

    companion object {
        @JvmStatic
        fun provideLocationCoordinatesAndExpectedLocationDescription(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    37.839184f, -25.793507f,
                    LocationDescription(
                        "Sete Cidades",
                        "Sete Cidades, Ponta Delgada, Açores, 9555-193, Portugal"
                    ),
                ),
                Arguments.of(
                    -23.839184f, 27.793507f,
                    LocationDescription(
                        "Lephalale Local Municipality",
                        "Lephalale Local Municipality, Waterberg District Municipality, Limpopo, South Africa"
                    )
                ),
            )
        }
    }

    @Test
    fun `It throws an exception when unable to geo locate`(): Unit = runBlocking {

        val fetchLocationDescription = FetchLocationDescriptionFromOSM(HTTPClientWithJavaHttpClient())

        assertThrows<UnableToFetchLocationDescription> {
            fetchLocationDescription.fromGeoCoordinates(-23.0f, -27.0f)
        }
    }
}