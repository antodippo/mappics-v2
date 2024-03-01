package com.antodippo.mappics.gallery

interface GalleryRepository {
    fun save(gallery: Gallery)
    fun list(): MutableMap<String, Gallery>
    fun getById(galleryId: String): Gallery
}