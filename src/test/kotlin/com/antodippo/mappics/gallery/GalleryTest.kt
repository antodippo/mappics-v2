package com.antodippo.mappics.gallery

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class GalleryTest {

    @Test
    fun `A gallery is created correctly`() {
        val gallery = Gallery("Test")
        assertEquals("Test", gallery.name)
        assertNotNull(gallery.id)
        assertNotNull(gallery.createdAt)
        assertEquals(0, gallery.pictures.count())
    }

    @Test
    fun `A picture can be added to a gallery`() {
        val gallery = Gallery("Test")
        assertEquals(0, gallery.pictures.count())

        gallery.addPicture(this.createPicture())
        assertEquals(1, gallery.pictures.count())

        gallery.addPicture(this.createPicture())
        assertEquals(2, gallery.pictures.count())
    }

    private fun createPicture(): Picture {
        return Picture(
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
    }
}