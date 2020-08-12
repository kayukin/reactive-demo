package com.example.interview.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

class GlobalExceptionHandlerTest {
    private WebTestClient webTestClient;
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        webTestClient = WebTestClient.bindToController(new Controller())
                .controllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void handleRuntimeException() {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.message").isEqualTo("test");
    }

    @RequestMapping("/")
    private static class Controller {
        @GetMapping
        public void produceError() {
            throw new RuntimeException("test");
        }
    }
}
