package com.example.interview.event;

import com.example.interview.model.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.LocalDate;

public class SseProviderTest {
    private SseProvider sseProvider;

    @BeforeEach
    void setUp() {
        sseProvider = new SseProvider();
    }

    @Test
    void testSubscribe() {
        Flux<ServerSentEvent<Article>> publisher = sseProvider.subscribe("testAuthor");
        Article expectedArticle = new Article(null, "test", "", LocalDate.now(), null, "testAuthor");

        StepVerifier.create(publisher)
                .then(() -> sseProvider.onArticleInsert(new ArticleEvent(expectedArticle)))
                .expectNextMatches(event -> event.data().equals(expectedArticle))
                .verifyTimeout(Duration.ofSeconds(3));
    }
}
