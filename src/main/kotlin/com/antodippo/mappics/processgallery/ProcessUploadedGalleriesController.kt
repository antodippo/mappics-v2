package com.antodippo.mappics.processgallery

import kotlinx.coroutines.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/process-galleries")
class ProcessUploadedGalleriesController(private val processUploadedGalleries: ProcessUploadedGalleries) {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var isProcessRunning = false

    @PostMapping("/start")
    fun startProcess(): String {
        return if (isProcessRunning) {
            "A process is already running"
        } else {
            isProcessRunning = true
            applicationScope.launch {
                processUploadedGalleries.process()
                isProcessRunning = false
            }
            "Process started"
        }
    }
}