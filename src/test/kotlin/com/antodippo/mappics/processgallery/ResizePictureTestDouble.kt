package com.antodippo.mappics.processgallery

import com.antodippo.mappics.galleryfilestorage.ResizePicture

class ResizePictureTestDouble: ResizePicture {
    override fun fromByteArrayAndDimensions(bytes: ByteArray, width: Int, height: Int): ByteArray {
        return byteArrayOf(1, 2, 3)
    }
}