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
}
