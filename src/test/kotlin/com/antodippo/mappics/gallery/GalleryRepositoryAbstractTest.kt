package com.antodippo.mappics.gallery

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.UUID

abstract class GalleryRepositoryAbstractTest(private val repository: GalleryRepository) {

    @Test
    fun `A gallery can be saved and retrieved`() {
        val gallery = Gallery("Test gallery")
        gallery.savePicture(PictureBuilder().createPicture())
        gallery.savePicture(PictureBuilder().createPicture())

        repository.save(gallery)
        //TODO Deal with eventual consistency
        Thread.sleep(2000)

        val retrievedGallery = repository.getById(gallery.id)
        assertEquals(gallery.id, retrievedGallery.id)
    }

    @Test
    fun `A list of galleries can be retrieved`() {
        val gallery = Gallery("Test gallery")
        val picture = PictureBuilder().createPicture()
        gallery.savePicture(picture)

        repository.save(gallery)
        repository.save(Gallery("Another test gallery"))
        //TODO Deal with eventual consistency
        Thread.sleep(2000)

        val galleries = repository.list()
        assertEquals(2, galleries.size)
        assertEquals("Test gallery", galleries[gallery.id]?.name)

        val pictures = galleries[gallery.id]!!.pictures
        assertEquals(1, pictures.count())
        assertEquals(picture.description, pictures[picture.id]!!.description)
    }

    @Test
    fun `An exception is thrown if gallery is not found`() {
        assertThrows(NullPointerException::class.java) {
            repository.getById(UUID.randomUUID().toString())
        }
    }
}

class GalleryRepositoryInMemoryTest: GalleryRepositoryAbstractTest(GalleryRepositoryInMemory())

@Disabled("This test uses Firestore on Google Cloud, it should be run locally and not in the CI pipeline.")
class GalleryRepositoryUsingFirestoreTest: GalleryRepositoryAbstractTest(GalleryRepositoryUsingFirestore()) {

    @AfterEach
    fun cleanUpEach() {
        val db = FirestoreOptions.getDefaultInstance().toBuilder()
            .setProjectId(firestoreProjectId)
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build()
            .service
        db.collection(firestoreCollection).get().get().forEach {
            db.collection(firestoreCollection).document(it.id).delete()
        }
    }
}
