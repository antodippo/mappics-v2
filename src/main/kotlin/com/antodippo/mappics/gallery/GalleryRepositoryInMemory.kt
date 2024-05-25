package com.antodippo.mappics.gallery

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("dev")
final class GalleryRepositoryInMemory: GalleryRepository {

    val galleries = mutableMapOf<String, Gallery>()

    override fun save(gallery: Gallery) {
        galleries[gallery.id] = gallery
    }

    override fun list(): Map<String, Gallery> {
        return galleries.toMap()
    }

    override fun getById(galleryId: String): Gallery {
        return galleries[galleryId]
            ?: throw GalleryNotFound("Gallery with id $galleryId not found")
    }

    override fun getBySlug(slug: String): Gallery {
        return galleries.values.firstOrNull { it.slug == slug }
            ?: throw GalleryNotFound("Gallery with slug $slug not found")
    }

    override fun getPictureFromFileName(galleryId: String, pictureFilename: String): Picture? {
        if (!galleries.containsKey(galleryId)) {
            return null
        }

        return galleries[galleryId]!!.pictures.values.firstOrNull { it.filename == pictureFilename }
    }
}