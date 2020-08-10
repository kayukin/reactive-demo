package com.example.interview.service;

import com.example.interview.model.Article;
import com.example.interview.persistence.ArticleRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final AuthorService authorService;

    public ArticleService(ArticleRepository articleRepository, AuthorService authorService) {
        this.articleRepository = articleRepository;
        this.authorService = authorService;
    }

    public Flux<Article> findAll() {
        return articleRepository.findAll()
                .flatMap(this::fetchAuthor);
    }

    public Mono<Article> findOne(String id) {
        return articleRepository.findById(id)
                .flatMap(this::fetchAuthor);
    }

    public Mono<Article> addNew(Article article) {
        return articleRepository.save(article)
                .flatMap(this::fetchAuthor);
    }

    public Mono<Void> delete(String id) {
        return articleRepository.deleteById(id);
    }

    public Mono<Article> update(Article article) {
        return Mono.justOrEmpty(article.getId())
                .flatMap(articleRepository::findById)
                .map(existing -> merge(existing, article))
                .flatMap(articleRepository::save)
                .flatMap(this::fetchAuthor);
    }

    private Mono<Article> fetchAuthor(Article article) {
        return Mono.justOrEmpty(article.getAuthorId())
                .flatMap(authorService::findOne)
                .map(article::withAuthor)
                .defaultIfEmpty(article);
    }

    private Article merge(Article target, Article source) {
        Optional.ofNullable(source.getAuthorId())
                .ifPresent(target::setAuthorId);
        Optional.ofNullable(source.getContent())
                .ifPresent(target::setContent);
        Optional.ofNullable(source.getDate())
                .ifPresent(target::setDate);
        Optional.ofNullable(source.getTitle())
                .ifPresent(target::setTitle);
        return target;
    }
}
