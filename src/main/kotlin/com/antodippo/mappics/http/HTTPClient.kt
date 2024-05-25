package com.antodippo.mappics.http

import java.net.URI
import java.net.http.HttpResponse

interface HTTPClient {
    suspend fun get(uri: URI): HttpResponse<String>
}