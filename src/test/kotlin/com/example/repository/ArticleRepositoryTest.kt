package com.example.repository

import com.example.model.Article
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleRepositoryTest(@Autowired val articleRepository: ArticleRepository) {

    @Test
    fun `should receive two articles`() {

        val firstArticle = Article(1, "First article", "Content of article 'First article'")
        val secondArticle = Article(2, "Second article", "Content of article 'Second article'")

        val articles = articleRepository.findAll().collectList().block()

        assertNotNull(articles)
        assertTrue(articles!!.contains(firstArticle))
        assertTrue(articles!!.contains(secondArticle))
    }

    @Test
    fun `should receive an article by id`() {
        val firstArticle = Article(1, "First article", "Content of article 'First article'")
        val article = articleRepository.findById(1).block()
        assertNotNull(article)
        assertEquals(article, firstArticle)
    }

    @Test
    fun `should create an article`() {

        val firstArticle = Article(1, "First article", "Content of article 'First article'")
        val secondArticle = Article(2, "Second article", "Content of article 'Second article'")
        val thirdArticle = Article(3, "Third article", "Content of article 'Third article'")

        articleRepository.create(thirdArticle)

        val articles = articleRepository.findAll().collectList().block()

        assertNotNull(articles)
        assertTrue(articles!!.contains(firstArticle))
        assertTrue(articles!!.contains(secondArticle))
        assertTrue(articles!!.contains(thirdArticle))
    }

    @Test
    fun `should update an article`() {

        val firstArticle = Article(1, "First article", "Content of article 'First article'")
        val secondArticle = Article(2, "Second article", "Content of article 'Second article'")
        val secondArticleNew = Article(2, "New title", "New content")

        articleRepository.update(secondArticle, secondArticleNew)

        val articles = articleRepository.findAll().collectList().block()

        assertNotNull(articles)
        assertTrue(articles!!.contains(firstArticle))
        assertTrue(articles!!.contains(secondArticleNew))
    }

    @Test
    fun `should delete an article by id`() {

        val firstArticle = Article(1, "First article", "Content of article 'First article'")
        val secondArticle = Article(2, "Second article", "Content of article 'Second article'")

        articleRepository.delete(secondArticle)

        val articles = articleRepository.findAll().collectList().block()

        assertNotNull(articles)
        assertTrue(articles!!.contains(firstArticle))
        assertFalse(articles!!.contains(secondArticle))
    }
}