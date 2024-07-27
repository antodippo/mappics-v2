package com.antodippo.mappics.galleryfilestorage

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.JpegWriter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ResizePictureWithScrimage: ResizePicture {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun fromByteArrayAndDimensions(bytes: ByteArray, width: Int, height: Int): ByteArray {

        logger.info("[Process galleries] Start reading picture")
        val pictureBuffer = ImmutableImage.loader().fromBytes(bytes)
        logger.info("[Process galleries] Finish reading picture")
        if (pictureBuffer.width <= width && pictureBuffer.height <= height){
            return bytes
        }

        logger.info("[Process galleries] Start generating resized picture {${pictureBuffer.width}x${pictureBuffer.height}} to {${width}x${height}}")
        val resizedImage = pictureBuffer.max(width, height).bytes(JpegWriter())
        logger.info("[Process galleries] Finish generating resized picture {${pictureBuffer.width}x${pictureBuffer.height}} to {${width}x${height}}")

        return resizedImage
    }
}