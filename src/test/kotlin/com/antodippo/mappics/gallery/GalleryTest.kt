package com.antodippo.mappics.gallery

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class GalleryTest {

    @Test
    fun `A gallery is created correctly`() {
        val gallery = Gallery("Test")
        assertEquals("Test", gallery.name)
        assertNotNull(gallery.id)
        assertNotNull(gallery.createdAt)
        assertEquals(0, gallery.pictures.count())
    }

    @ParameterizedTest
    @MethodSource("provideGalleryNamesAndSlugs")
    fun `A gallery is created with the correct slug`(name: String, expectedSlug: String) {
        val gallery = Gallery(name)
        assertEquals(expectedSlug, gallery.slug)
    }

    @Test
    fun `A picture can be added to a gallery`() {
        val gallery = Gallery("Test")
        assertEquals(0, gallery.pictures.count())

        gallery.savePicture(PictureBuilder().createPicture())
        assertEquals(1, gallery.pictures.count())

        gallery.savePicture(PictureBuilder().createPicture())
        assertEquals(2, gallery.pictures.count())
    }

    @Test
    fun `A picture can be updated in a gallery`() {
        val gallery = Gallery("Test")
        assertEquals(0, gallery.pictures.count())

        val picture = PictureBuilder().createPicture()
        gallery.savePicture(picture)
        assertEquals(1, gallery.pictures.count())

        picture.description = "An updated description"
        gallery.savePicture(picture)
        assertEquals(1, gallery.pictures.count())
        assertEquals("An updated description", gallery.pictures[picture.id.toString()]?.description)
    }

    companion object {
        @JvmStatic
        fun provideGalleryNamesAndSlugs(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("Roma", "roma"),
                Arguments.of("San Diego", "san-diego"),
                Arguments.of("Ho chi minh", "ho-chi-minh"),
            );
        }
    }
}