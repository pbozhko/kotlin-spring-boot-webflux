package com.example.repository

import com.example.model.Article
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.Flux
import java.util.HashMap

@Component
class ArticleRepository {

    private val articles = HashMap<Int, Article>()
    private var idCounter: Int = 1

    init {
        listOf("First article", "Second article").forEach {
            title -> run {
                val id = this.idCounter++
            this.articles.put(id, Article(id, title, "Content of article '$title'"))
            }
        }
    }

    fun findAll(): Flux<Article> {
        return Flux.fromIterable(this.articles.values)
    }

    fun findById(id: Int): Mono<Article> {
        return Mono.just(this.articles.get(id)!!)
    }

    fun delete(article: Article): Mono<Void> {
        if(this.articles.containsKey(article.id)) {
            this.articles.remove(article.id)
        }
        return Mono.empty()
    }

    fun create(article: Article): Mono<Article> {
        val id = this.idCounter++
        article.id = id
        this.articles.put(id, article)
        return Mono.just(article)
    }

    fun update(oldArticle: Article, newArticle: Article): Mono<Article> {
        newArticle.id = oldArticle.id
        this.articles.put(oldArticle.id!!, newArticle)
        return Mono.just(newArticle)
    }
}