package com.example.interview.controller;

import com.example.interview.model.Author;
import com.example.interview.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
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
