package com.antodippo.mappics.galleryfilestorage

interface GalleryFileStorage {
    fun listUploadedGalleries(): List<String>

}