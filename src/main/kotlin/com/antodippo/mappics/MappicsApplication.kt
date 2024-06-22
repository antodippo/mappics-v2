package com.antodippo.mappics

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class MappicsApplication

fun main(args: Array<String>) {
	//TODO find a better way to load this
	if (System.getenv("spring.profiles.active") == "dev") {
		dotenv().entries().map { System.setProperty(it.key, it.value) }
	}

	runApplication<MappicsApplication>(*args)
}
