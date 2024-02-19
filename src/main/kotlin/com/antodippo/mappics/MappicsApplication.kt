package com.antodippo.mappics

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.storage.StorageOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MappicsApplication

fun main(args: Array<String>) {
	runApplication<MappicsApplication>(*args)

	val firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
		.setProjectId("mappics-215209")
		.setCredentials(GoogleCredentials.getApplicationDefault())
		.build()

	val db: Firestore = firestoreOptions.service

	db.collection("test")
		.document("test3")
		.set(mapOf("name" to "Anto3", "age" to 202))

	val storage = StorageOptions.getDefaultInstance().service
	val bucket = storage.get("mappics-test")
	bucket.create("uploads/test3.txt", "Hello, World!33333".toByteArray())
	for (blob in bucket.list().iterateAll()) {
		println(blob.toString())
	}
}
