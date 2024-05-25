package com.antodippo.mappics.processgallery

import com.antodippo.mappics.gallery.GalleryRepositoryInMemory
import com.antodippo.mappics.galleryfilestorage.GalleryFileStorageWithLocalFileSystem
import com.antodippo.mappics.http.HTTPClientThatAlwaysReturns
import com.antodippo.mappics.http.HttpDummyResponse
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class ProcessUploadedGalleriesTest {

    @Test
    fun `Uploaded galleries are processed`() = runBlocking {

        // Arrange
        val galleryRepository = GalleryRepositoryInMemory()
        val galleryFileStorage = GalleryFileStorageWithLocalFileSystem("uploads/")

        val fetchLocationDescription = FetchLocationDescriptionFromOSM(HTTPClientThatAlwaysReturns(
            HttpDummyResponse(200, """
                {
                    "name": "Some city",
                    "display_name": "Some city, some county, some state, some country",
                    "address": {
                        "city": "Some city",
                        "county": "Some county",
                        "state": "Some state",
                        "country": "Some country"
                    }
                }
            """)
        ))

        val fetchWeatherData = FetchWeatherDataFromVisualCrossing(HTTPClientThatAlwaysReturns(
            HttpDummyResponse(200, """
                {
                    "days": [{
                        "location": {
                            "name": "Some place",
                            "address": "Some place",
                            "latitude": 32.7157,
                            "longitude": -117.1611,
                            "tz": "America/Los_Angeles"
                        },
                        "currentConditions": {
                            "datetime": "2021-08-01T12:00:00",
                            "icon": "partly-cloudy-day",
                            "temperature": 25.0,
                            "description": "Partly cloudy"
                        },
                        "conditions": "Partly cloudy",
                        "temp": 10.0,
                        "humidity": 10.0,
                        "pressure": 10.0,
                        "windspeed": 10
                    }]
                }
            """)
        ))

        // Act
        val processUploadedGalleries = ProcessUploadedGalleries(
            galleryFileStorage,
            ExtractExifDataWithMetadataExtractor(),
            CreatePicturesForGallery(
                resizePicture = ResizePictureTestDouble(),
                galleryFileStorage = galleryFileStorage,
                uploadsDirectory = "uploads/",
                resizedDirectory = "resized/",
                thumbnailsDirectory = "thumbnails/"
            ),
            fetchLocationDescription,
            fetchWeatherData,
            galleryRepository,
        )

        processUploadedGalleries.process()

        // Assert
        val galleries = galleryRepository.list().values
        // 3 galleries: Italy, Azores, Iceland
        assertEquals(3, galleries.size)

        val italyGallery = galleryRepository.getBySlug("italy")
        assertEquals(2, italyGallery.pictures.size)
        italyGallery.pictures.forEach { (_, picture) ->
            assertTrue(picture.resizedFilename.startsWith("resized/Italy/"))
            assertTrue(picture.thumbnailFilename.startsWith("thumbnails/Italy/"))
            assertFalse(picture.needsDescription())
            assertFalse(picture.needsWeatherData())
        }

        val azoresGallery = galleryRepository.getBySlug("azores")
        assertEquals(1, azoresGallery.pictures.size)
        azoresGallery.pictures.forEach { (_, picture) ->
            assertTrue(picture.resizedFilename.startsWith("resized/Azores/"))
            assertTrue(picture.thumbnailFilename.startsWith("thumbnails/Azores/"))
            assertFalse(picture.needsDescription())
            assertFalse(picture.needsWeatherData())
        }

        val icelandGallery = galleryRepository.getBySlug("iceland")
        assertEquals(2, icelandGallery.pictures.size)
        icelandGallery.pictures.forEach { (_, picture) ->
            assertTrue(picture.resizedFilename.startsWith("resized/Iceland/"))
            assertTrue(picture.thumbnailFilename.startsWith("thumbnails/Iceland/"))
            assertFalse(picture.needsDescription())
            assertFalse(picture.needsWeatherData())
        }
    }

    @Test
    fun `Uploaded galleries are processed even if external services return errors`() = runBlocking {

        // Arrange
        val galleryRepository = GalleryRepositoryInMemory()
        val galleryFileStorage = GalleryFileStorageWithLocalFileSystem("uploads/")

        val fetchLocationDescription = FetchLocationDescriptionFromOSM(HTTPClientThatAlwaysReturns(
            HttpDummyResponse(500, """ { "error": "Internal server error" } """)
        ))

        val fetchWeatherData = FetchWeatherDataFromVisualCrossing(HTTPClientThatAlwaysReturns(
            HttpDummyResponse(500, """ { "error": "Internal server error" } """)
        ))

        // Act
        val processUploadedGalleries = ProcessUploadedGalleries(
            galleryFileStorage,
            ExtractExifDataWithMetadataExtractor(),
            CreatePicturesForGallery(
                resizePicture = ResizePictureTestDouble(),
                galleryFileStorage = galleryFileStorage,
                uploadsDirectory = "uploads/",
                resizedDirectory = "resized/",
                thumbnailsDirectory = "thumbnails/"
            ),
            fetchLocationDescription,
            fetchWeatherData,
            galleryRepository,
        )

        processUploadedGalleries.process()

        // Assert
        val galleries = galleryRepository.list().values
        // 3 galleries: Italy, Azores, Iceland
        assertEquals(3, galleries.size)

        val italyGallery = galleryRepository.getBySlug("italy")
        assertEquals(2, italyGallery.pictures.size)

        val pictureWithNoGPSCoordinates = galleryRepository.getPictureFromFileName(italyGallery.id, "uploads/Italy/DSC_0401.JPG")!!
        assertTrue(pictureWithNoGPSCoordinates.resizedFilename.startsWith("resized/Italy/"))
        assertTrue(pictureWithNoGPSCoordinates.thumbnailFilename.startsWith("thumbnails/Italy/"))
        assertFalse(pictureWithNoGPSCoordinates.needsDescription())
        assertFalse(pictureWithNoGPSCoordinates.needsWeatherData())

        val pictureWithGPSCoordinates = galleryRepository.getPictureFromFileName(italyGallery.id, "uploads/Italy/DSC_0118.JPG")!!
        assertTrue(pictureWithGPSCoordinates.resizedFilename.startsWith("resized/Italy/"))
        assertTrue(pictureWithGPSCoordinates.thumbnailFilename.startsWith("thumbnails/Italy/"))
        assertTrue(pictureWithGPSCoordinates.needsDescription())
        assertTrue(pictureWithGPSCoordinates.needsWeatherData())

        val azoresGallery = galleryRepository.getBySlug("azores")
        assertEquals(1, azoresGallery.pictures.size)
        azoresGallery.pictures.forEach { (_, picture) ->
            assertTrue(picture.resizedFilename.startsWith("resized/Azores/"))
            assertTrue(picture.thumbnailFilename.startsWith("thumbnails/Azores/"))
            assertTrue(picture.needsDescription())
            assertTrue(picture.needsWeatherData())
        }

        val icelandGallery = galleryRepository.getBySlug("iceland")
        assertEquals(2, icelandGallery.pictures.size)
        icelandGallery.pictures.forEach { (_, picture) ->
            assertTrue(picture.resizedFilename.startsWith("resized/Iceland/"))
            assertTrue(picture.thumbnailFilename.startsWith("thumbnails/Iceland/"))
            assertTrue(picture.needsDescription())
            assertTrue(picture.needsWeatherData())
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