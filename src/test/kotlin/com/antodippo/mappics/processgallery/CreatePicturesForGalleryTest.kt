package com.antodippo.mappics.processgallery

import com.antodippo.mappics.galleryfilestorage.GalleryFileStorageInMemory
import com.antodippo.mappics.galleryfilestorage.UploadedPicture
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CreatePicturesForGalleryTest {

    @Test
    fun `A resized version of a picture and its thumbnail are created and stored`() = runBlocking {

        val galleryFileStorage = GalleryFileStorageInMemory(mutableMapOf())
        val createPicturesForGallery = CreatePicturesForGallery(
            resizePicture = ResizePictureTestDouble(),
            galleryFileStorage = galleryFileStorage,
            uploadsDirectory = "uploads/",
            resizedDirectory = "resized/",
            thumbnailsDirectory = "thumbnails/"
        )

        val filenames = createPicturesForGallery.fromUploadedPicture(
            UploadedPicture("uploads/rome/img_123.jpg", byteArrayOf(4,5,6))
        )

        assertEquals("resized/rome/img_123.jpg", filenames.resizedFilename)
        assertEquals(1, galleryFileStorage.storedResizedPictures.size)
        assertTrue(galleryFileStorage.storedResizedPictures.containsKey("resized/rome/img_123.jpg"))
        assertArrayEquals(byteArrayOf(1,2,3), galleryFileStorage.storedResizedPictures["resized/rome/img_123.jpg"])

        assertEquals("thumbnails/rome/img_123.jpg", filenames.thumbnailFilename)
        assertEquals(1, galleryFileStorage.storedThumbnails.size)
        assertTrue(galleryFileStorage.storedThumbnails.containsKey("thumbnails/rome/img_123.jpg"))
        assertArrayEquals(byteArrayOf(1,2,3), galleryFileStorage.storedThumbnails["thumbnails/rome/img_123.jpg"])
    }
}