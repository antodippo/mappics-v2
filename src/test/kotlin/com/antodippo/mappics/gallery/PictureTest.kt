package com.antodippo.mappics.gallery

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class PictureTest {

    @Test
    fun `A picture is created correctly`() {
        val picture = Picture(
            "test/test.jpg",
            ExifData(
                make = "Canon",
                model = "EOS 5D Mark IV",
                exposure = "1/100",
                aperture = "f/2.8",
                iso = "100",
                focalLength = "50mm",
                gpsLatitude = 0.0f,
                gpsLongitude = 0.0f,
                gpsAltitude = 0.0f,
                takenAt = Date()
            )
        )

        assertEquals("test/test.jpg", picture.filename)
        assertNotNull(picture.id)
        assertNotNull(picture.createdAt)
        assertEquals("Canon", picture.exifData.make)
        assertEquals("EOS 5D Mark IV", picture.exifData.model)
        assertEquals("1/100", picture.exifData.exposure)
        assertEquals("f/2.8", picture.exifData.aperture)
        assertEquals("100", picture.exifData.iso)
        assertEquals("50mm", picture.exifData.focalLength)
        assertEquals(0.0f, picture.exifData.gpsLatitude)
        assertEquals(0.0f, picture.exifData.gpsLongitude)
        assertEquals(0.0f, picture.exifData.gpsAltitude)
        assertNotNull(picture.exifData.takenAt)
    }

    @Test
    fun `A picture can have a description`() {
        val picture = PictureBuilder().createPicture()
        assertEquals("Test picture", picture.description)
        picture.description = "A different test picture"
        assertEquals("A different test picture", picture.description)
    }

    @Test
    fun `A picture can have a long description`() {
        val picture = PictureBuilder().createPicture()
        assertEquals("This is a test picture", picture.longDescription)
        picture.longDescription = "This is a different test picture"
        assertEquals("This is a different test picture", picture.longDescription)
    }

    @Test
    fun `A picture can have a resized filename`() {
        val picture = PictureBuilder().createPicture()
        assertEquals("", picture.resizedFilename)
        picture.resizedFilename = "test/test_resized.jpg"
        assertEquals("test/test_resized.jpg", picture.resizedFilename)
    }

    @Test
    fun `A picture can have a thumbnail filename`() {
        val picture = PictureBuilder().createPicture()
        assertEquals("", picture.thumbnailFilename)
        picture.thumbnailFilename = "test/test_thumbnail.jpg"
        assertEquals("test/test_thumbnail.jpg", picture.thumbnailFilename)
    }

    @Test
    fun `A picture can have weather data`() {
        val picture = PictureBuilder().createPicture()
        assertEquals("", picture.weather.description)
        assertEquals(0.0f, picture.weather.temperature)
        assertEquals(0.0f, picture.weather.humidity)
        assertEquals(0.0f, picture.weather.pressure)
        assertEquals(0.0f, picture.weather.windSpeed)

        picture.weather = WeatherData(
            description = "Sunny",
            temperature = 25.0f,
            humidity = 50.0f,
            pressure = 1013.0f,
            windSpeed = 10.0f,
        )
        assertEquals("Sunny", picture.weather.description)
        assertEquals(25.0f, picture.weather.temperature)
        assertEquals(50.0f, picture.weather.humidity)
        assertEquals(1013.0f, picture.weather.pressure)
        assertEquals(10.0f, picture.weather.windSpeed)
    }

    @Test
    fun `Weather data can't have invalid humidity`() {
        assertThrows(IllegalArgumentException::class.java) {
            WeatherData(
                description = "Sunny",
                temperature = 25.0f,
                humidity = 101.0f,
                pressure = 1013.0f,
                windSpeed = 10.0f,
            )
        }
    }

    @Test
    fun `Exif data can't have invalid latitude`() {
        assertThrows(IllegalArgumentException::class.java) {
            ExifData(
                make = "Canon",
                model = "EOS 5D Mark IV",
                exposure = "1/100",
                aperture = "f/2.8",
                iso = "100",
                focalLength = "50mm",
                gpsLatitude = 91.0f,
                gpsLongitude = 0.0f,
                gpsAltitude = 0.0f,
                takenAt = Date()
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ExifData(
                make = "Canon",
                model = "EOS 5D Mark IV",
                exposure = "1/100",
                aperture = "f/2.8",
                iso = "100",
                focalLength = "50mm",
                gpsLatitude = -91.0f,
                gpsLongitude = 0.0f,
                gpsAltitude = 0.0f,
                takenAt = Date()
            )
        }
    }

    @Test
    fun `Exif data can't have invalid longitude`() {
        assertThrows(IllegalArgumentException::class.java) {
            ExifData(
                make = "Canon",
                model = "EOS 5D Mark IV",
                exposure = "1/100",
                aperture = "f/2.8",
                iso = "100",
                focalLength = "50mm",
                gpsLatitude = 0.0f,
                gpsLongitude = 181.0f,
                gpsAltitude = 0.0f,
                takenAt = Date()
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ExifData(
                make = "Canon",
                model = "EOS 5D Mark IV",
                exposure = "1/100",
                aperture = "f/2.8",
                iso = "100",
                focalLength = "50mm",
                gpsLatitude = 0.0f,
                gpsLongitude = -181.0f,
                gpsAltitude = 0.0f,
                takenAt = Date()
            )
        }
    }

    @Test
    fun `Exif data can't have invalid altitude`() {
        assertThrows(IllegalArgumentException::class.java) {
            ExifData(
                make = "Canon",
                model = "EOS 5D Mark IV",
                exposure = "1/100",
                aperture = "f/2.8",
                iso = "100",
                focalLength = "50mm",
                gpsLatitude = 0.0f,
                gpsLongitude = 0.0f,
                gpsAltitude = -1.0f,
                takenAt = Date()
            )
        }
    }
}
