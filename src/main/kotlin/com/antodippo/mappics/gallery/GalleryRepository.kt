package com.antodippo.mappics.gallery

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

interface GalleryRepository {
    fun save(gallery: Gallery)
    fun list(): Map<String, Gallery>
    fun getById(galleryId: String): Gallery
    fun getBySlug(slug: String): Gallery
    fun getPictureFromFileName(galleryId: String, pictureFilename: String): Picture?
}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class GalleryNotFound(message: String) : Exception(message)