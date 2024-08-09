package com.antodippo.mappics.galleryfilestorage

interface GalleryFileStorage {
    fun listUploadedGalleries(): List<UploadedGallery>
    fun savePicture(filename: String, content: ByteArray)
}

data class UploadedGallery(
    val name: String,
    val pictures: MutableList<UploadedPicture>
)

data class UploadedPicture(
    val filename: String,
    val contentProvider: () -> ByteArray
) {
    val content: ByteArray by lazy { contentProvider() }
}