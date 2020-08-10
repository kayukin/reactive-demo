package com.example.interview.controller;

import com.example.interview.model.Author;
import com.example.interview.service.AuthorService;
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
    public Mono<Author> findOne(@PathVariable String id) {
        return authorService.findOne(id);
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
    public Mono<Author> update(@RequestBody Author author) {
        return authorService.update(author);
    }
}
