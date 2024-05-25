package com.antodippo.mappics.processgallery

import com.antodippo.mappics.gallery.ExifData
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifIFD0Directory
import com.drew.metadata.exif.ExifSubIFDDirectory
import com.drew.metadata.exif.GpsDirectory
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.util.*

@Service
class ExtractExifDataWithMetadataExtractor: ExtractExifData {

    override fun fromContent(content: ByteArray): ExifData {

        val inputStream = ByteArrayInputStream(content)
        val metadata = ImageMetadataReader.readMetadata(inputStream)
        val exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
        val exifSubIfdDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory::class.java)
        val gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory::class.java)

        return ExifData(
            make = exifDirectory?.getString(ExifIFD0Directory.TAG_MAKE) ?: "",
            model = exifDirectory?.getString(ExifIFD0Directory.TAG_MODEL) ?: "",
            exposure = exifSubIfdDirectory?.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME) ?: "",
            aperture = exifSubIfdDirectory?.getString(ExifSubIFDDirectory.TAG_FNUMBER) ?: "",
            iso = exifSubIfdDirectory?.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT) ?: "",
            focalLength = exifSubIfdDirectory?.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH) ?: "",
            gpsLatitude = gpsDirectory?.geoLocation?.latitude?.toFloat() ?: 0.0f,
            gpsLongitude = gpsDirectory?.geoLocation?.longitude?.toFloat() ?: 0.0f,
            gpsAltitude = gpsDirectory?.getRational(GpsDirectory.TAG_ALTITUDE)?.toFloat() ?: 0.0f,
            takenAt = exifDirectory?.getDate(ExifIFD0Directory.TAG_DATETIME) ?: Date()
        )
    }
}