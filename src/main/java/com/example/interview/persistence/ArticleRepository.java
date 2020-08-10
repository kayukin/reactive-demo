package com.example.interview.persistence;

import com.example.interview.model.Article;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ArticleRepository extends ReactiveMongoRepository<Article, String> {
    Mono<Long> countAllByAuthorId(String authorId);
}
