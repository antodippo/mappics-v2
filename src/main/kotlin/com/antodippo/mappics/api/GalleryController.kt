package com.antodippo.mappics.api

import com.antodippo.mappics.gallery.Gallery
import com.antodippo.mappics.gallery.GalleryUseCases
import com.antodippo.mappics.gallery.Picture
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/galleries")
class GalleryController(@Autowired private val galleryUseCases: GalleryUseCases) {

    @GetMapping
    fun listGalleries(): List<Gallery> {
        return galleryUseCases.getAllGalleries()
    }

    @GetMapping("/{slug}/pictures")
    fun listPicturesInGallery(@PathVariable slug: String): List<Picture> {
        return galleryUseCases.getGalleryBySlug(slug).pictures.values.toList()
    }
}