package com.example.interview.controller;

import com.example.interview.event.SseProvider;
import com.example.interview.model.Article;
import com.example.interview.model.Author;
import com.example.interview.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final SseProvider sseProvider;

    public AuthorController(AuthorService authorService, SseProvider sseProvider) {
        this.authorService = authorService;
        this.sseProvider = sseProvider;
    }

    @GetMapping
    public Flux<Author> findAll() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Author>> findOne(@PathVariable String id) {
        return authorService.findOne(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/subscribe")
    public Flux<ServerSentEvent<Article>> subscribe(@PathVariable String id) {
        return sseProvider.subscribe(id);
    }

    @PostMapping
    public Mono<Author> addNew(@RequestBody Author author) {
        return authorService.addNew(author);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return authorService.delete(id);
    }

    @PatchMapping
    public Mono<ResponseEntity<Author>> update(@RequestBody Author author) {
        return authorService.update(author)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
