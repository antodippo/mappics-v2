package com.antodippo.mappics

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MappicsApplication

fun main(args: Array<String>) {
	dotenv().entries().map { System.setProperty(it.key, it.value) }
	runApplication<MappicsApplication>(*args)
}
