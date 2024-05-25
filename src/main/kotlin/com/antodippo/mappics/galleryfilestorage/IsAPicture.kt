package com.antodippo.mappics.galleryfilestorage

fun isAPicture(filename: String): Boolean
{
    return filename.endsWith(suffix = ".JPG", ignoreCase = true)
        || filename.endsWith(suffix = ".JPEG", ignoreCase = true)
}