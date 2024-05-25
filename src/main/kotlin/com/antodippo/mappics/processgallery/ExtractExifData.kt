package com.antodippo.mappics.processgallery

import com.antodippo.mappics.gallery.ExifData

interface ExtractExifData {
    fun fromContent(content: ByteArray): ExifData
}