package com.antodippo.mappics.galleryfilestorage

import com.google.cloud.storage.StorageOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("prod")
class GalleryFileStorageWithGoogleStorage(
    @Value("\${googlestorage.bucketName}") private val bucketName: String,
    @Value("\${googlestorage.uploadsDirectory}") private val uploadsDirectory: String,
): GalleryFileStorage {

    private val bucket = StorageOptions.getDefaultInstance().service.get(bucketName)!!
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun listUploadedGalleries(): List<UploadedGallery> {

        logger.info("[Process galleries] Start listing galleries from Google Storage")
        val galleries = bucket.list().iterateAll()
            .filter {
                // it should be in the uploads directory
                it.blobId.name.startsWith(uploadsDirectory)
                // we're not interested in the uploads directory itself
                && !it.blobId.name.equals(uploadsDirectory)
                // we're not interested in subdirectories
                && !it.isDirectory
                // it should be a JPEG file
                && isAPicture(it.blobId.name)
            }
            .groupBy {
                // group by gallery name
                it.blobId.name.split("/")[1]
            }
        logger.info("[Process galleries] Finish listing galleries from Google Storage")

        logger.info("[Process galleries] Start returning galleries in a list")
        return galleries.map { (galleryName, pictureBlobs) ->
            UploadedGallery(
                galleryName,
                // TODO check if content is too big to be loaded in memory
                pictureBlobs.map { UploadedPicture(it.blobId.name, it.getContent()) }.toMutableList()
            )
        }
    }

    override fun savePicture(filename: String, content: ByteArray) {
        logger.info("[Process galleries] Saving picture $filename to Google Storage")
        bucket.create(filename, content)
        logger.info("[Process galleries] Picture $filename saved to Google Storage")
    }
}