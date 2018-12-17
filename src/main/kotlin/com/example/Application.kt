package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
@SpringBootApplication
class Application {

    @Bean
    fun route() = router {
        GET("/test") { _ -> ServerResponse.ok().body(BodyInserters.fromObject(arrayOf(1, 2, 3))) }
    }
}


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}