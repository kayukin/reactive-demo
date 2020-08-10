package com.example.interview.controller;

import com.example.interview.model.Article;
import com.example.interview.service.ArticleService;
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
    public Mono<Article> findOne(@PathVariable String id) {
        return articleService.findOne(id);
    }

    @PostMapping
    public Mono<Article> addNew(@RequestBody Article article) {
        return articleService.addNew(article);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return articleService.delete(id);
    }
}
