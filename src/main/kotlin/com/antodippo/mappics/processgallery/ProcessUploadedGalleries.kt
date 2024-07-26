package com.antodippo.mappics.processgallery

import com.antodippo.mappics.gallery.*
import com.antodippo.mappics.galleryfilestorage.GalleryFileStorage
import com.antodippo.mappics.galleryfilestorage.UploadedPicture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProcessUploadedGalleries(
    private val galleryFileStorage: GalleryFileStorage,
    private val extractExifData: ExtractExifData,
    private val createPicturesForGallery: CreatePicturesForGallery,
    private val fetchLocationDescription: FetchLocationDescription,
    private val fetchWeatherData: FetchWeatherData,
    private val galleryRepository: GalleryRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun process() {

        val uploadedGalleries = galleryFileStorage.listUploadedGalleries()
        uploadedGalleries.forEach { uploadedGallery ->

            val gallery = Gallery(uploadedGallery.name)
            logger.info("[Process galleries] Start GALLERY: ${gallery.name}: ${System.currentTimeMillis()}")
            uploadedGallery.pictures.map { uploadedPicture ->
                val picture = this@ProcessUploadedGalleries.getOrCreatePicture(gallery, uploadedPicture)
                logger.info("[Process galleries] Start PICTURE: ${gallery.name}.${picture.filename}: ${System.currentTimeMillis()}")

                val descriptionDeferred = CoroutineScope(Dispatchers.IO).async { fetchLocationDescription(picture) }
                val weatherDataDeferred = CoroutineScope(Dispatchers.IO).async { fetchWeatherData(picture) }
                val locationDescription = descriptionDeferred.await()
                val weatherData = weatherDataDeferred.await()

                picture.description = locationDescription.shortDescription
                picture.longDescription = locationDescription.longDescription
                picture.weather = weatherData
                gallery.savePicture(picture)

                logger.info("[Process galleries] Finish PICTURE: ${gallery.name}.${picture.filename}: ${System.currentTimeMillis()}")
            }

            if(gallery.pictures.isNotEmpty()) this@ProcessUploadedGalleries.galleryRepository.save(gallery)
            logger.info("[Process galleries] Finish GALLERY: ${gallery.name}: ${System.currentTimeMillis()}")
        }
    }

    private suspend fun getOrCreatePicture(gallery: Gallery, uploadedPicture: UploadedPicture): Picture {
        var picture = this.galleryRepository.getPictureFromFileName(gallery.id, uploadedPicture.filename)

        if (picture == null) {
            val filenames = this.createPicturesForGallery.fromUploadedPicture(uploadedPicture)

            val exifData = try {
                this.extractExifData.fromContent(uploadedPicture.content)
            } catch (e: Exception) { ExifData() }

            picture = Picture(uploadedPicture.filename, exifData)
            picture.resizedFilename = filenames.resizedFilename
            picture.thumbnailFilename = filenames.thumbnailFilename
        }

        return picture
    }

    private suspend fun fetchLocationDescription(picture: Picture): LocationDescription {
        logger.info("[Process galleries] Start description ${picture.filename}: ${System.currentTimeMillis()}")
        var locationDescription = LocationDescription("", "")
        if (picture.needsDescription()) {
            try {
                locationDescription = this.fetchLocationDescription.fromGeoCoordinates(
                    picture.exifData.gpsLatitude,
                    picture.exifData.gpsLongitude
                )
            } catch (e: UnableToFetchLocationDescription) {
                logger.error("[Process galleries] Unable to fetch description data for picture ${picture.id}", e)
            }
        }
        logger.info("[Process galleries] Finish description ${picture.filename}: ${System.currentTimeMillis()}")

        return locationDescription
    }

    private suspend fun fetchWeatherData(picture: Picture): WeatherData {
        logger.info("[Process galleries] Start weather ${picture.filename}: ${System.currentTimeMillis()}")
        var weatherData = WeatherData()
        if (picture.needsWeatherData()) {
            try {
                weatherData = this.fetchWeatherData.fromGeoCoordinatesAndDatetime(
                    picture.exifData.gpsLatitude,
                    picture.exifData.gpsLongitude,
                    picture.exifData.takenAt
                )
            } catch (e: UnableToFetchWeatherData) {
                logger.error("[Process galleries] Unable to fetch weather data for picture ${picture.id}", e)
            }
        }
        logger.info("[Process galleries] Finish weather ${picture.filename}: ${System.currentTimeMillis()}")

        return weatherData
    }
}