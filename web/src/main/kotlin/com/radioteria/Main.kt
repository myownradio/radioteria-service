package com.radioteria

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ImportResource

fun main(args: Array<String>) {
    SpringApplication.run(BootStrapConfiguration::class.java, *args)
}

@EnableAutoConfiguration
@ImportResource(locations = arrayOf("classpath:application-context.xml"))
class BootStrapConfiguration
