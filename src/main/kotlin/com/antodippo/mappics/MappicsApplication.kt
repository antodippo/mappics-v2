package com.antodippo.mappics

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
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
		.document("test2")
		.set(mapOf("name" to "Anto2", "age" to 202))
}
