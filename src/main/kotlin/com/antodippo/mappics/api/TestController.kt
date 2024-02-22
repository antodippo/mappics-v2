package com.antodippo.mappics.api

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/test")
class HealthCheckController {

    @GetMapping("/")
    fun healthCheck(): String {
        return "Ciao!"
    }
}