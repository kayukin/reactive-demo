package com.example.interview.controller;

import com.example.interview.model.Article;
import com.example.interview.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {
    private ArticleController articleController;
    private WebTestClient webTestClient;
    @Mock
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        articleController = new ArticleController(articleService);
        webTestClient = WebTestClient.bindToController(articleController)
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testFindAll() {
        when(articleService.findAll()).thenReturn(Flux.just(new Article(), new Article()));

        webTestClient.get()
                .uri("/articles/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Article.class).hasSize(2);
    }

    @Test
    void testFindOne() {
        when(articleService.findOne("id")).thenReturn(Mono.just(new Article()));

        webTestClient.get()
                .uri("/articles/id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testAddNew() {
        when(articleService.addNew(any())).then(inv -> Mono.just(inv.getArgument(0)));

        webTestClient.post()
                .uri("/articles/")
                .bodyValue(new Article())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testUpdate() {
        when(articleService.update(any())).then(inv -> Mono.just(inv.getArgument(0)));

        webTestClient.patch()
                .uri("/articles/")
                .bodyValue(new Article())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testDelete() {
        webTestClient.delete()
                .uri("/articles/id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
        verify(articleService).delete("id");
    }
}
