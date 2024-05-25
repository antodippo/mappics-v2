package com.antodippo.mappics.galleryfilestorage

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test")
class GalleryFileStorageInMemory(
    private val uploadedGalleries: MutableMap<String, UploadedGallery>
) : GalleryFileStorage {

    val storedResizedPictures = mutableMapOf<String, ByteArray>()
    val storedThumbnails = mutableMapOf<String, ByteArray>()

    override fun listUploadedGalleries(): List<UploadedGallery> {
        return this.uploadedGalleries.values.toList()
    }

    override fun savePicture(filename: String, content: ByteArray) {

        if (!isAPicture(filename)) return

        val filenameList = filename.split("/")

        if (filenameList[0] == "uploads") {
            val uploadedPicture = UploadedPicture(filename, content)
            val galleryName = filenameList[1]

            val gallery = this.uploadedGalleries[galleryName] ?: UploadedGallery(galleryName, mutableListOf())
            gallery.pictures.addLast(uploadedPicture)

            this.uploadedGalleries[galleryName] = gallery

            return
        }

        when (filenameList[0]) {
            "resized" -> this.storedResizedPictures[filename] = content
            "thumbnails" -> this.storedThumbnails[filename] = content
            else -> throw IllegalArgumentException("Invalid filename starting with ${filenameList[0]}")
        }
    }
}