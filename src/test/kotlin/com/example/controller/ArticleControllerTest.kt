package com.example.controller

import com.example.model.Article
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleControllerTest(@Autowired val webTestClient: WebTestClient) {

    @Test
    fun `should get two articles`() {

        val firstArticle = Article(1, "First article", "Content of article 'First article'")
        val secondArticle = Article(2, "Second article", "Content of article 'Second article'")

        webTestClient.get().uri("/articles")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/stream+json;charset=UTF-8")
                .expectBodyList(Article::class.java)
                .hasSize(2).contains(firstArticle).contains(secondArticle)
    }

    @Test
    fun `should get an article by id`() {

        val firstArticle = Article(1, "First article", "Content of article 'First article'")

        webTestClient.get().uri("/articles/1")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/stream+json;charset=UTF-8")
                .expectBodyList(Article::class.java)
                .hasSize(1).contains(firstArticle).contains(firstArticle)
    }

    @Test
    fun `should create new article`() {

        val firstArticle = Article(1, "First article", "Content of article 'First article'")
        val secondArticle = Article(2, "Second article", "Content of article 'Second article'")

        webTestClient.get().uri("/articles")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/stream+json;charset=UTF-8")
                .expectBodyList(Article::class.java)
                .hasSize(2).contains(firstArticle).contains(secondArticle)

        val thirdArticle = Article(3, "Third article", "Content of article 'Third article'")

        webTestClient.post().uri("/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .body(Mono.just(thirdArticle), Article::class.java)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/stream+json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.title").isEqualTo("Third article")
                .jsonPath("$.content").isEqualTo("Content of article 'Third article'")

        webTestClient.get().uri("/articles")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/stream+json;charset=UTF-8")
                .expectBodyList(Article::class.java)
                .hasSize(3).contains(firstArticle).contains(secondArticle).contains(thirdArticle)
    }

    @Test
    fun `should update article`() {

        val article = Article(1, "New title", "New content")

        webTestClient.put().uri("/articles/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .body(Mono.just(article), Article::class.java)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/stream+json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.title").isEqualTo("New title")
                .jsonPath("$.content").isEqualTo("New content")

        webTestClient.get().uri("/articles")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/stream+json;charset=UTF-8")
                .expectBodyList(Article::class.java)
                .hasSize(2).contains(article)
    }

    @Test
    fun `should delete article by id`() {
        val firstArticle = Article(1, "First article", "Content of article 'First article'")

        webTestClient.delete().uri("/articles/2")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty()

        webTestClient.get().uri("/articles")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/stream+json;charset=UTF-8")
                .expectBodyList(Article::class.java)
                .hasSize(1).contains(firstArticle)
    }
}