package com.antodippo.mappics.galleryfilestorage

interface ResizePicture {

    suspend fun fromByteArrayAndDimensions(bytes: ByteArray, width: Int, height: Int): ByteArray
}