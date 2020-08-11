package com.example.interview.service;

import com.example.interview.model.Author;
import com.example.interview.persistence.ArticleRepository;
import com.example.interview.persistence.AuthorRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final ArticleRepository articleRepository;

    public AuthorService(AuthorRepository authorRepository, ArticleRepository articleRepository) {
        this.authorRepository = authorRepository;
        this.articleRepository = articleRepository;
    }

    public Flux<Author> findAll() {
        return authorRepository.findAll()
                .flatMap(this::countArticles);
    }

    public Mono<Author> findOne(String id) {
        return authorRepository.findById(id)
                .flatMap(this::countArticles);
    }

    public Mono<Author> addNew(Author author) {
        return authorRepository.save(author)
                .flatMap(this::countArticles);
    }

    public Mono<Void> delete(String id) {
        return authorRepository.deleteById(id);
    }

    public Mono<Author> update(Author author) {
        return Mono.justOrEmpty(author.getId())
                .flatMap(authorRepository::findById)
                .map(existing -> merge(existing, author))
                .flatMap(authorRepository::save)
                .flatMap(this::countArticles);
    }

    private Author merge(Author target, Author source) {
        Optional.ofNullable(source.getName())
                .ifPresent(target::setName);
        Optional.ofNullable(source.getDateOfBirth())
                .ifPresent(target::setDateOfBirth);
        return target;
    }

    private Mono<Author> countArticles(Author author) {
        return articleRepository.countAllByAuthorId(author.getId())
                .map(author::withArticlesCount);
    }

    public Flux<Author> findByIds(Flux<String> authorIds) {
        return authorRepository.findAllById(authorIds);
    }
}
