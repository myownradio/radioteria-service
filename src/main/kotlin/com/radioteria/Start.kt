package com.radioteria

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource

@Configuration
@SpringBootApplication
@ImportResource(locations = arrayOf("classpath:application-context.xml"))
open class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

