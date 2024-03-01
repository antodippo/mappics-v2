package com.antodippo.mappics.gallery

import java.util.*

class PictureBuilder {

    fun createPicture(): Picture {
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
        picture.description = "Test picture"
        picture.longDescription = "This is a test picture"

        return picture
    }
}