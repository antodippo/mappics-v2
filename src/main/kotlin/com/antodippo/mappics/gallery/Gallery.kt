package com.antodippo.mappics.gallery

import java.util.*

class Gallery(val name: String) {

    val id: String = UUID.randomUUID().toString()
    val createdAt: Date = Date()
    val slug = name.lowercase().replace(" ", "-")
    var pictures = HashMap<String, Picture>()

    // No argument constructor for Firestore
    constructor(): this("")

    //TODO test this?
    @Synchronized
    fun savePicture(picture: Picture) {
        this.pictures[picture.id] = picture
    }
}