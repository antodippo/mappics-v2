package com.antodippo.mappics.galleryfilestorage

import com.antodippo.mappics.galleryfilestorage.ResizePictureWithScrimage
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

class ResizePictureWithScrimageTest {

    @Test
    fun `It resizes a picture`() = runBlocking {
        val filePath = Paths.get("src/test/resources/galleries/Azores/DSC_0892.JPG")
        val pictureBytes = Files.readAllBytes(filePath)

        val pictureBuffer = ImageIO.read(ByteArrayInputStream(pictureBytes))
        assertEquals(5984, pictureBuffer.width)
        assertEquals(3366, pictureBuffer.height)

        val resizedPictureBytes = ResizePictureWithScrimage().fromByteArrayAndDimensions(pictureBytes, 100, 100)

        val resizedPictureBuffer = ImageIO.read(ByteArrayInputStream(resizedPictureBytes))
        assertEquals(100, resizedPictureBuffer.width)
        assertEquals(56, resizedPictureBuffer.height)
    }

    @Test
    fun `It doesn't resize a picture that's already small enough`() = runBlocking {
        val filePath = Paths.get("src/test/resources/galleries/Azores/DSC_0892.JPG")
        val pictureBytes = Files.readAllBytes(filePath)

        val pictureBuffer = ImageIO.read(ByteArrayInputStream(pictureBytes))
        assertEquals(5984, pictureBuffer.width)
        assertEquals(3366, pictureBuffer.height)

        val resizedPictureBytes = ResizePictureWithScrimage().fromByteArrayAndDimensions(pictureBytes, 6000, 4000)

        val resizedPictureBuffer = ImageIO.read(ByteArrayInputStream(resizedPictureBytes))
        assertEquals(5984, resizedPictureBuffer.width)
        assertEquals(3366, resizedPictureBuffer.height)

    }
}