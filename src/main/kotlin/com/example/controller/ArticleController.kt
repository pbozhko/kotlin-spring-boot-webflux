package com.example.controller

import com.example.model.Article
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ArticleController {
    fun all(): Flux<Article>
    fun get(id: Int): Mono<ResponseEntity<Article>>
    fun create(article: Article): Mono<ResponseEntity<Article>>
    fun update(id: Int, article: Article): Mono<ResponseEntity<Article>>
    fun delete(id: Int): Mono<ResponseEntity<Void>>
}
