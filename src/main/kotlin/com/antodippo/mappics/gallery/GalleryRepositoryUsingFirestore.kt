package com.antodippo.mappics.gallery

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions

//TODO put the project id and collection in a configuration file
const val firestoreProjectId = "mappics-215209"
const val firestoreCollection = "galleries-test"

class GalleryRepositoryUsingFirestore: GalleryRepository {

    private val db = FirestoreOptions.getDefaultInstance().toBuilder()
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
            ?: throw NullPointerException()
    }

    override fun list(): MutableMap<String, Gallery> {
        val galleries = mutableMapOf<String, Gallery>()
        db.collection(firestoreCollection).get().get().forEach {
            val gallery = it.toObject(Gallery::class.java)
            galleries[gallery.id] = gallery
        }

        return galleries
    }
}