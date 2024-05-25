package com.antodippo.mappics.gallery

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/galleries")
class GalleryController(private val galleryUseCases: GalleryUseCases) {

    @GetMapping
    fun listGalleries(): List<Gallery> {
        return galleryUseCases.getAllGalleries()
    }

    @GetMapping("/{slug}")
    fun getGallery(@PathVariable slug: String): Gallery {
        return galleryUseCases.getGalleryBySlug(slug)
    }

    @GetMapping("/{slug}/pictures")
    fun listPicturesInGallery(@PathVariable slug: String): List<Picture> {
        return galleryUseCases.getGalleryBySlug(slug).pictures.values.toList()
    }
}