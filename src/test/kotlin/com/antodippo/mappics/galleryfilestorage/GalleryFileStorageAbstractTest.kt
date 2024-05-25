package com.antodippo.mappics.galleryfilestorage

import com.google.cloud.storage.StorageOptions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

abstract class GalleryFileStorageAbstractTest(private val galleryFileStorage: GalleryFileStorage) {

    //TODO align with GalleryFileStorageWithLocalFileSystem?
    @BeforeEach
    fun setup() {
        this.galleryFileStorage.savePicture("uploads/azores/DSC_0892.JPG", byteArrayOf(1, 2, 3))
        this.galleryFileStorage.savePicture("uploads/azores/DSC_0893.JPG", byteArrayOf(4, 5, 6))
        this.galleryFileStorage.savePicture("uploads/azores/not-a-picture", byteArrayOf(0, 0, 0))
        this.galleryFileStorage.savePicture("uploads/new york/001.jpeg", byteArrayOf(7, 8, 9))
    }

    @Test
    fun `It lists uploaded galleries and pictures`() {
        val uploadedGalleries = this.galleryFileStorage.listUploadedGalleries()

        assertEquals(2, uploadedGalleries.size)
        assertEquals("azores", uploadedGalleries[0].name)
        assertEquals("new york", uploadedGalleries[1].name)

        assertEquals(2, uploadedGalleries[0].pictures.size)
        assertEquals("uploads/azores/DSC_0892.JPG", uploadedGalleries[0].pictures[0].filename)
        assertEquals("uploads/azores/DSC_0893.JPG", uploadedGalleries[0].pictures[1].filename)
    }

    @Test
    fun `It saves a picture`() {
        this.galleryFileStorage.savePicture("uploads/iceland/DSC_1234.JPG", byteArrayOf(1, 2, 3))

        val uploadedGalleries = this.galleryFileStorage.listUploadedGalleries()
        assertEquals(3, uploadedGalleries.size)

        val icelandGallery = uploadedGalleries.find { it.name == "iceland"}!!
        assertEquals("iceland", icelandGallery.name)
        assertEquals(1, icelandGallery.pictures.size)
        assertEquals("uploads/iceland/DSC_1234.JPG", icelandGallery.pictures[0].filename)
        assertArrayEquals(byteArrayOf(1, 2, 3), icelandGallery.pictures[0].content)
    }
}

class GalleryFileStorageInMemoryTest : GalleryFileStorageAbstractTest(GalleryFileStorageInMemory(mutableMapOf()))

@Disabled("This test uses Google Storage, it should be run locally and not in the CI pipeline.")
class GalleryFileStorageWithGoogleStorageTest : GalleryFileStorageAbstractTest(
    GalleryFileStorageWithGoogleStorage("mappics-test", "uploads/")
) {

    @AfterEach
    fun tearDown() {
        val bucket = StorageOptions.getDefaultInstance().service.get("mappics-test")!!
        bucket.list().iterateAll()
            .filter { !it.blobId.name.equals("uploads/") }
            .forEach { it.delete() }
    }
}
