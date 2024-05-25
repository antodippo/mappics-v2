package com.antodippo.mappics.galleryfilestorage

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.JpegWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

@Service
class ResizePictureWithScrimage: ResizePicture {

    override suspend fun fromByteArrayAndDimensions(bytes: ByteArray, width: Int, height: Int): ByteArray {

        val pictureBuffer = withContext(Dispatchers.IO) {
            ImageIO.read(ByteArrayInputStream(bytes))
        }

        if (pictureBuffer.width <= width && pictureBuffer.height <= height){
            return bytes
        }

        return ImmutableImage.loader()
            .fromBytes(bytes)
            .max(width, height)
            .bytes(JpegWriter())
    }
}