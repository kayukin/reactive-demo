package com.example.interview.service;

import com.example.interview.model.Article;
import com.example.interview.model.Author;
import com.example.interview.persistence.ArticleRepository;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private AuthorService authorService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        articleService = new ArticleService(articleRepository, authorService, eventPublisher);
    }

    @Test
    void testFindAll() {
        when(articleRepository.findAll()).thenReturn(Flux.just(testArticle(), testArticle()));
        when(authorService.findByIds(any())).thenReturn(Flux.just(testAuthor()));

        Flux<Article> articles = articleService.findAll();

        StepVerifier.create(articles)
                .assertNext(this::assertTestArticle)
                .assertNext(this::assertTestArticle)
                .verifyComplete();
    }

    @Test
    void testFindOne() {
        when(articleRepository.findById("id")).thenReturn(Mono.just(testArticle()));
        when(authorService.findOne(any())).thenReturn(Mono.just(testAuthor()));

        Mono<Article> article = articleService.findOne("id");

        StepVerifier.create(article)
                .assertNext(this::assertTestArticle)
                .verifyComplete();
    }

    @Test
    void testDelete() {
        articleService.delete("id");

        verify(articleRepository).deleteById("id");
    }

    @Test
    void testAddNew() {
        Article article = testArticle().withAuthor(testAuthor());
        when(articleRepository.save(any())).thenReturn(Mono.just(article));
        when(authorService.findOne(any())).thenReturn(Mono.just(article.getAuthor()));

        Mono<Article> result = articleService.addNew(article);

        StepVerifier.create(result)
                .expectNextMatches(article::equals)
                .verifyComplete();

    }

    @Test
    void testUpdate() {
        Article article = testArticle().withId("id");
        when(articleRepository.findById("id")).thenReturn(Mono.just(article));
        when(articleRepository.save(any())).then(inv -> Mono.just(inv.getArgument(0)));
        when(authorService.findOne(any())).thenReturn(Mono.just(testAuthor()));

        Mono<Article> result = articleService.update(article.withTitle("new title"));

        StepVerifier.create(result)
                .assertNext(a -> {
                    Assertions.assertEquals("new title", a.getTitle());
                    Assertions.assertEquals(article.getContent(), a.getContent());
                    Assertions.assertEquals(article.getDate(), a.getDate());
                })
                .verifyComplete();
    }

    private Author testAuthor() {
        return new Author("authorId", "test", LocalDate.now().minusYears(40), 2L);
    }

    private Article testArticle() {
        Lorem lorem = LoremIpsum.getInstance();
        return new Article(null, lorem.getTitle(4), lorem.getParagraphs(2, 3),
                LocalDate.now(), null, "authorId");
    }

    private void assertTestArticle(Article article) {
        Assertions.assertNotNull(article.getAuthor());
        Assertions.assertEquals("test", article.getAuthor().getName());
    }
}
