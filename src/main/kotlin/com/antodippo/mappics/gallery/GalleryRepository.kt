package com.antodippo.mappics.gallery

interface GalleryRepository {
    fun save(gallery: Gallery)
    fun list(): Map<String, Gallery>
    fun getById(galleryId: String): Gallery
    fun getBySlug(slug: String): Gallery
}