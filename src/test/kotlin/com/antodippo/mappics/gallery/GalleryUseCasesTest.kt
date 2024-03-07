package com.antodippo.mappics.gallery

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GalleryUseCasesTest {

    @Test
    fun `A gallery can be retrieved by slug`() {
        val galleryRepository = GalleryRepositoryInMemory()
        val gallery = Gallery("San Diego")
        galleryRepository.save(gallery)

        val galleryUseCases = GalleryUseCases(galleryRepository)
        val retrievedGallery = galleryUseCases.getGalleryBySlug("san-diego")

        assertEquals(gallery, retrievedGallery)
    }

    @Test
    fun `It throws an exception when gallery is not found`() {
        val galleryUseCases = GalleryUseCases(GalleryRepositoryInMemory())
        assertThrows<GalleryNotFound> { galleryUseCases.getGalleryBySlug("galway") }
    }

    @Test
    fun `List of galleries can be retrieved`() {
        val galleryRepository = GalleryRepositoryInMemory()
        galleryRepository.save(Gallery("Quito"))
        galleryRepository.save(Gallery("Melbourne"))

        val galleryUseCases = GalleryUseCases(galleryRepository)
        val retrievedGalleries = galleryUseCases.getAllGalleries()

        assertEquals(2, retrievedGalleries.size)
    }

    @Test
    fun `Empty list of galleries is returned`() {
        val galleryUseCases = GalleryUseCases(GalleryRepositoryInMemory())
        val retrievedGalleries = galleryUseCases.getAllGalleries()

        assertEquals(0, retrievedGalleries.size)
    }
}