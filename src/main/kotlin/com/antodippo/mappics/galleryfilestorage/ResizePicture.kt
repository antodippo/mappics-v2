package com.antodippo.mappics.galleryfilestorage

interface ResizePicture {

    fun fromByteArrayAndDimensions(bytes: ByteArray, width: Int, height: Int): ByteArray
}