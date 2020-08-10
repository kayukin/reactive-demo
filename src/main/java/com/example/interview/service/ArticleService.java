package com.example.interview.service;

import com.example.interview.model.Article;
import com.example.interview.persistence.ArticleRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    private Mono<Article> fetchAuthor(Article article) {
        return authorService.findOne(article.getAuthorId())
                .map(author1 -> {
                    article.setAuthor(author1);
                    return article;
                });
    }
}
