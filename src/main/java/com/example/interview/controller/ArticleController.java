package com.example.interview.controller;

import com.example.interview.model.Article;
import com.example.interview.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public Flux<Article> findAll() {
        return articleService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Article>> findOne(@PathVariable String id) {
        return articleService.findOne(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<Article> addNew(@RequestBody Article article) {
        return articleService.addNew(article);
    }

    @PatchMapping
    public Mono<ResponseEntity<Article>> update(@RequestBody Article article) {
        return articleService.update(article)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return articleService.delete(id);
    }
}
