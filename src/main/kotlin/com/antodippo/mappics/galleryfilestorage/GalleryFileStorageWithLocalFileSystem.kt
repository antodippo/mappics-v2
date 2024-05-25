package com.antodippo.mappics.galleryfilestorage

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.io.File

@Service
@Profile("dev")
class GalleryFileStorageWithLocalFileSystem(
    @Value("\${googlestorage.uploadsDirectory}") private val uploadsDirectory: String,
): GalleryFileStorage {

    private val localGalleriesDirectory = "galleries/"

    override fun listUploadedGalleries(): List<UploadedGallery> {
        val directory = File(localGalleriesDirectory + uploadsDirectory)
        val galleryDirectories = directory.listFiles()
            ?.filter { it.isDirectory }
            ?.toList()
            ?: emptyList()

        return galleryDirectories.map { galleryDir ->
            val pictures = galleryDir.listFiles()
                ?.filter { pictureFile ->
                    isAPicture(pictureFile.name)
                }
                ?.map { pictureFile ->
                    val path = galleryDir.toString().replace(localGalleriesDirectory + uploadsDirectory, uploadsDirectory)
                    UploadedPicture("$path/${pictureFile.name}", pictureFile.readBytes())
                }
                ?.toMutableList() ?: mutableListOf()

            UploadedGallery(galleryDir.name, pictures)
        }
    }

    override fun savePicture(filename: String, content: ByteArray) {
        val file = File(localGalleriesDirectory + filename)
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeBytes(content)
    }
}