package com.example.interview.controller;

import com.example.interview.event.SseProvider;
import com.example.interview.model.Article;
import com.example.interview.model.Author;
import com.example.interview.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static com.example.interview.TestUtil.testAuthor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {
    private AuthorController authorController;
    private WebTestClient webTestClient;

    @Mock
    private AuthorService authorService;
    @Mock
    private SseProvider sseProvider;

    @BeforeEach
    void setUp() {
        authorController = new AuthorController(authorService, sseProvider);
        webTestClient = WebTestClient.bindToController(authorController)
                .controllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testFindAll() {
        when(authorService.findAll()).thenReturn(Flux.just(testAuthor()));

        webTestClient.get()
                .uri("/authors/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].name").isEqualTo("test");
    }

    @Test
    void testFindOne() {
        when(authorService.findOne("id")).thenReturn(Mono.just(testAuthor()));

        webTestClient.get()
                .uri("/authors/id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void testSubscribe() {
        when(sseProvider.subscribe("id")).thenReturn(Flux.just(ServerSentEvent.<Article>builder().build()));
        FluxExchangeResult<ServerSentEvent> result = webTestClient.get()
                .uri("/authors/id/subscribe")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ServerSentEvent.class);

        StepVerifier.create(result.getResponseBody())
                .expectNextMatches(Objects::nonNull)
                .thenCancel()
                .verify();
    }

    @Test
    void testAddNew() {
        when(authorService.addNew(any())).then(inv -> Mono.just(inv.getArgument(0)));

        webTestClient.post()
                .uri("/authors/")
                .bodyValue(testAuthor())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Author.class)
                .isEqualTo(testAuthor());
    }

    @Test
    void testDelete() {
        webTestClient.delete()
                .uri("/authors/id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        verify(authorService).delete("id");
    }

    @Test
    void testUpdate() {
        when(authorService.update(any())).then(inv -> Mono.just(inv.getArgument(0)));

        webTestClient.patch()
                .uri("/authors/")
                .bodyValue(testAuthor())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }
}
