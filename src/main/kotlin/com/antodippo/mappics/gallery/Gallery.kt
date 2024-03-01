package com.antodippo.mappics.gallery

import java.util.*

class Gallery(val name: String) {

    val id: String = UUID.randomUUID().toString()
    val createdAt: Date = Date()
    val slug = name.lowercase().replace(" ", "-")
    var pictures = mapOf<String, Picture>()

    // No argument constructor for Firestore
    constructor(): this("")

    fun savePicture(picture: Picture) {
        pictures = pictures.plus(Pair(picture.id, picture))
    }
}