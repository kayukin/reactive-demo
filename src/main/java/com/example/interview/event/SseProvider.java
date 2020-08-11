package com.example.interview.event;

import com.example.interview.model.Article;
import org.springframework.context.event.EventListener;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class SseProvider {
    private static final String ARTICLE_EVENT = "new-article-event";
    private Map<String, EmitterProcessor<Article>> processorMap;

    public SseProvider() {
        this.processorMap = new HashMap<>();
    }

    public Flux<ServerSentEvent<Article>> subscribe(String authorId) {
        return processorMap.computeIfAbsent(authorId, s -> EmitterProcessor.create())
                .map(article -> ServerSentEvent.<Article>builder()
                        .id(UUID.randomUUID().toString())
                        .data(article)
                        .event(ARTICLE_EVENT)
                        .build());
    }

    @EventListener
    @Async
    public void onArticleInsert(ArticleEvent event) {
        EmitterProcessor<Article> processor = processorMap.get(event.getSource().getAuthorId());
        if (processor != null) {
            processor.onNext(event.getSource());
        }
    }
}
