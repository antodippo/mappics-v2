package com.antodippo.mappics.gallery

import java.util.*

class Gallery(val name: String) {
    val id: GalleryId = GalleryId()
    val createdAt: Date = Date()
    var pictures = listOf<Picture>()
    fun addPicture(picture: Picture) {
        pictures = pictures.plus(picture)
    }
}

class GalleryId {
    private val id: UUID = UUID.randomUUID()

    override fun toString(): String {
        return id.toString()
    }
}