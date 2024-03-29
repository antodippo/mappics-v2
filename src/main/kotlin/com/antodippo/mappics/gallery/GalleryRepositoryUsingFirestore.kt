package com.antodippo.mappics.gallery

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

//TODO put the project id and collection in a configuration file
const val firestoreProjectId = "mappics-215209"
const val firestoreCollection = "galleries-test"

@Service
@Profile("prod")
final class GalleryRepositoryUsingFirestore: GalleryRepository {

    val db: Firestore = FirestoreOptions.getDefaultInstance().toBuilder()
        .setProjectId(firestoreProjectId)
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build()
        .service

    override fun save(gallery: Gallery) {
        db.collection(firestoreCollection)
            .document(gallery.id)
            .set(gallery)
    }

    override fun getById(galleryId: String): Gallery {
        return db.collection(firestoreCollection)
            .document(galleryId)
            .get().get()
            .toObject(Gallery::class.java)
            ?: throw GalleryNotFound("Gallery with id $galleryId not found")
    }

    override fun getBySlug(slug: String): Gallery {
        return db.collection(firestoreCollection)
            .whereEqualTo("slug", slug)
            .get()
            .get()
            .firstOrNull()
            ?.toObject(Gallery::class.java)
            ?: throw GalleryNotFound("Gallery with slug $slug not found")
    }

    override fun list(): Map<String, Gallery> {
        val galleries = mutableMapOf<String, Gallery>()
        db.collection(firestoreCollection)
            .get()
            .get()
            .forEach {
            val gallery = it.toObject(Gallery::class.java)
            galleries[gallery.id] = gallery
        }

        return galleries.toMap()
    }
}