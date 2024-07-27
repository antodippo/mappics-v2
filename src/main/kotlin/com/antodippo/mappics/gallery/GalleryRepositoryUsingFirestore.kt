package com.antodippo.mappics.gallery

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("prod")
final class GalleryRepositoryUsingFirestore(
    @Value("\${firestore.projectId}") private val firestoreProjectId: String,
    @Value("\${firestore.collection}") private val firestoreCollection: String,
): GalleryRepository {

    private val db: Firestore

    init {
        db = FirestoreOptions.getDefaultInstance().toBuilder()
            .setProjectId(firestoreProjectId)
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .setRetrySettings(
                FirestoreOptions.getDefaultRetrySettings().toBuilder()
                    .setMaxAttempts(10)
                    .setTotalTimeout(org.threeten.bp.Duration.ofMinutes(5))
                    .build()
            )
            .build()
            .service
    }

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

    override fun getPictureFromFileName(galleryId: String, pictureFilename: String): Picture? {
        try {
            val gallery = this.getById(galleryId)
            return gallery.pictures.values.firstOrNull { it.filename == pictureFilename }
        } catch (e: GalleryNotFound) {
            return null
        }
    }
}