package com.antodippo.mappics.galleryfilestorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import org.junit.jupiter.params.provider.Arguments

class IsAPictureTest {

    @ParameterizedTest
    @MethodSource("providePicturesAndNonPictures")
    fun `isAPicture returns correct result`(filename: String, expectedResult: Boolean) {
        val result = isAPicture(filename)
        assertEquals(expectedResult, result)
    }

    companion object {
        @JvmStatic
        fun providePicturesAndNonPictures(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("picture.JPG", true),
                Arguments.of("picture.JPEG", true),
                Arguments.of("picture.jpg", true),
                Arguments.of("picture.jpeg", true),
                Arguments.of("not-a-picture.png", false),
                Arguments.of("not-a-picture.gif", false),
                Arguments.of("not-a-picture.txt", false),
                Arguments.of("not-a-picture", false)
            )
        }
    }
}