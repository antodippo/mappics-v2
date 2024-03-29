package com.antodippo.mappics.gallery

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GalleryUseCases(@Autowired private val galleryRepository: GalleryRepository) {

    fun getGalleryBySlug(slug: String): Gallery {
        return this.galleryRepository.getBySlug(slug)
    }

    fun getAllGalleries(): List<Gallery> {
        return this.galleryRepository.list().values.toList()
    }
}