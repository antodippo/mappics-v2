package com.antodippo.mappics.gallery

class GalleryRepositoryInMemory: GalleryRepository {

    private val galleries = mutableMapOf<String, Gallery>()

    override fun save(gallery: Gallery) {
        galleries[gallery.id] = gallery
    }

    override fun list(): MutableMap<String, Gallery> {
        return galleries
    }

    override fun getById(galleryId: String): Gallery {
        return galleries[galleryId] ?: throw NullPointerException()
    }
}