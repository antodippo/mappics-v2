package com.antodippo.mappics.processgallery

import com.antodippo.mappics.galleryfilestorage.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class CreatePicturesForGallery(
    private val resizePicture: ResizePicture,
    private val galleryFileStorage: GalleryFileStorage,
    @Value("\${googlestorage.uploadsDirectory}") private val uploadsDirectory: String,
    @Value("\${googlestorage.resizedDirectory}") private val resizedDirectory: String,
    @Value("\${googlestorage.thumbnailsDirectory}") private val thumbnailsDirectory: String,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val resizedPictureMaxWidth = 2048
    private val resizedPictureMaxHeight = 2048
    private val thumbnailPictureMaxWidth = 300
    private val thumbnailPictureMaxHeight = 300

    fun fromUploadedPicture(uploadedPicture: UploadedPicture): Filenames {

        logger.info("[Process galleries] Start resizing picture: ${uploadedPicture.filename}")

        val resizedPictureBytes = this.resizePicture.fromByteArrayAndDimensions(
            uploadedPicture.content, resizedPictureMaxWidth, resizedPictureMaxHeight
        )
        val resizedPictureFilename = uploadedPicture.filename.replace(uploadsDirectory, resizedDirectory)
        this.galleryFileStorage.savePicture(resizedPictureFilename, resizedPictureBytes)

        val thumbnailPictureBytes = this.resizePicture.fromByteArrayAndDimensions(
            uploadedPicture.content, thumbnailPictureMaxWidth, thumbnailPictureMaxHeight
        )
        val thumbnailPictureFilename = uploadedPicture.filename.replace(uploadsDirectory, thumbnailsDirectory)
        this.galleryFileStorage.savePicture(thumbnailPictureFilename, thumbnailPictureBytes)

        logger.info("[Process galleries] Finish resizing picture: ${uploadedPicture.filename}")

        return Filenames(resizedPictureFilename, thumbnailPictureFilename)
    }
}

data class Filenames(val resizedFilename: String, val thumbnailFilename: String)