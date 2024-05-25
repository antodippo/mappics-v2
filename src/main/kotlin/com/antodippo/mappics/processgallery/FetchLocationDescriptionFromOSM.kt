package com.antodippo.mappics.processgallery

import com.antodippo.mappics.http.HTTPClient
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import java.net.URI

@Service
class FetchLocationDescriptionFromOSM(private val client: HTTPClient): FetchLocationDescription {

    override suspend fun fromGeoCoordinates(latitude: Float, longitude: Float): LocationDescription {

        val response = client.get(URI("https://nominatim.openstreetmap.org/reverse?lat=$latitude&lon=$longitude&zoom=13&format=jsonv2"))

        if (response.statusCode() != 200) {
            throw UnableToFetchLocationDescription("Unable to fetch location description - lat: $latitude - lon: $longitude - message: ${response.body()}")
        }

        val jsonBody = jacksonObjectMapper().readValue<JsonNode>(response.body())

        if (jsonBody["error"] != null) {
            throw UnableToFetchLocationDescription(jsonBody["error"].asText() + " - lat: $latitude - lon: $longitude")
        }

        return LocationDescription(jsonBody["name"].asText(), jsonBody["display_name"].asText())
    }
}