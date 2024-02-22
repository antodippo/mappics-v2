package com.antodippo.mappics.api

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.storage.StorageOptions
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/test")
class HealthCheckController {

    @GetMapping("/")
    fun healthCheck(): String {
        return "Ciao!"
    }

    @GetMapping("/firestore")
    fun googleFirestore(): String {
        val db = FirestoreOptions.getDefaultInstance().toBuilder()
            .setProjectId("mappics-215209")
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build()
            .service

        db.collection("test")
            .document(UUID.randomUUID().toString())
            .set(mapOf("name" to "Anto", "age" to 222))

        var documents = ""
        db.collection("test")
            .get()
            .get()
            .forEach { documents += it.toString() }

        return documents
    }

    @GetMapping("/storage")
    fun googleStorage(): String {
        val storage = StorageOptions.getDefaultInstance().service
        val bucket = storage.get("mappics-test")
        bucket.create(
            "uploads/test-" + UUID.randomUUID().toString() + ".txt",
            "Hello, World!".toByteArray()
        )

        var files = ""
        for (blob in bucket.list().iterateAll()) {
            files += blob.toString()
        }

        return files
    }
}