package com.antodippo.mappics.processgallery

import com.antodippo.mappics.gallery.ExifData
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Stream

class ExtractExifDataWithMetadataExtractorTest {

    @ParameterizedTest
    @MethodSource("providePicturesAndExpectedExifData")
    fun `It reads exif data`(path: String, expectedExifData: ExifData) {
        val filePath = Paths.get(path)
        val fileContent = Files.readAllBytes(filePath)

        val exifData = ExtractExifDataWithMetadataExtractor().fromContent(fileContent)

        assertEquals(expectedExifData, exifData)
    }

    companion object {

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        @JvmStatic
        fun providePicturesAndExpectedExifData(): Stream<Arguments> {
            return Stream.of(
                // Standard case
                Arguments.of(
                    "src/test/resources/galleries/Azores/DSC_0892.JPG",
                    ExifData(
                        make = "Sony",
                        model = "F5121",
                        exposure = "1/1000",
                        aperture = "2",
                        iso = "40",
                        focalLength = "4.23",
                        gpsLatitude = 37.839184f,
                        gpsLongitude = -25.793507f,
                        gpsAltitude = 616f,
                        takenAt = this.dateFormat.parse("2017-08-24 14:07:14")
                    )
                ),
                // No GPS info
                Arguments.of(
                    "src/test/resources/galleries/Italy/DSC_0401.JPG",
                    ExifData(
                        make = "Sony",
                        model = "F5121",
                        exposure = "1/2000",
                        aperture = "2",
                        iso = "40",
                        focalLength = "4.23",
                        gpsLatitude = 0.0f,
                        gpsLongitude = 0.0f,
                        gpsAltitude = 0f,
                        takenAt = this.dateFormat.parse("2017-03-26 16:31:47")
                    )
                ),
            )
        }
    }
}