package com.antodippo.mappics.gallery

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class GalleryNotFound(message: String) : Exception(message)