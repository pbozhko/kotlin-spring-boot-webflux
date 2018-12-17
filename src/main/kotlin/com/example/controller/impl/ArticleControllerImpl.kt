package com.example.controller.impl

import com.example.controller.ArticleController
import com.example.model.Article
import com.example.repository.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/articles"])
class ArticleControllerImpl: ArticleController {

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @GetMapping(value = [""], produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    override fun all(): Flux<Article> {
        return this.articleRepository.findAll()
    }

    @GetMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    override fun get(@PathVariable(value = "id") id: Int): Mono<ResponseEntity<Article>> {
        return this.articleRepository
                .findById(id)
                .map {
                    article -> ResponseEntity.ok(article)
                }
                .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = [""], produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    override fun create(@Valid @RequestBody article: Article): Mono<ResponseEntity<Article>> {
        return this.articleRepository.create(article).map {
            article -> ResponseEntity.ok(article)
        }
    }

    @PutMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    override fun update(@PathVariable(value = "id") id: Int,
                        @Valid @RequestBody newArticle: Article): Mono<ResponseEntity<Article>> {
        return this.articleRepository
                .findById(id)
                .flatMap {
                    oldArticle -> this.articleRepository
                                .update(oldArticle, newArticle)
                                .flatMap {
                                    newArticle -> Mono.just(ResponseEntity(newArticle, HttpStatus.OK))
                                }
                }
                .defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
    }

    @DeleteMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    override fun delete(@PathVariable(value = "id") id: Int): Mono<ResponseEntity<Void>> {
        return this.articleRepository
                .findById(id)
                .flatMap {
                    article -> this.articleRepository
                        .delete(article)
                        .then(Mono.just(ResponseEntity<Void>(HttpStatus.OK)))
                }.defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
    }
}