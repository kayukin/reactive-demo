package com.example.interview.service;

import com.example.interview.model.Author;
import com.example.interview.persistence.ArticleRepository;
import com.example.interview.persistence.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.example.interview.TestUtil.testAuthor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService(authorRepository, articleRepository);
    }

    @Test
    void testFindAll() {
        when(authorRepository.findAll()).thenReturn(Flux.just(testAuthor(), testAuthor()));
        when(articleRepository.countAllByAuthorId(any())).thenReturn(Mono.just(1L));
        Flux<Author> result = authorService.findAll();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void testFindOne() {
        when(authorRepository.findById("id")).thenReturn(Mono.just(testAuthor()));
        when(articleRepository.countAllByAuthorId(any())).thenReturn(Mono.just(2L));

        Mono<Author> result = authorService.findOne("id");

        StepVerifier.create(result)
                .expectNext(testAuthor())
                .verifyComplete();
    }

    @Test
    void testAddNew() {
        when(authorRepository.save(any())).thenReturn(Mono.just(testAuthor()));
        when(articleRepository.countAllByAuthorId(any())).thenReturn(Mono.just(2L));

        Mono<Author> result = authorService.addNew(testAuthor());

        StepVerifier.create(result)
                .expectNext(testAuthor())
                .verifyComplete();
    }

    @Test
    void testDelete() {
        when(authorRepository.deleteById("id")).thenReturn(Mono.empty());

        Mono<Void> result = authorService.delete("id");

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void testUpdate() {
        when(authorRepository.save(any())).thenReturn(Mono.just(testAuthor()));
        when(authorRepository.findById("authorId")).thenReturn(Mono.just(testAuthor()));
        when(articleRepository.countAllByAuthorId(any())).thenReturn(Mono.just(2L));

        Mono<Author> result = authorService.update(testAuthor());

        StepVerifier.create(result)
                .expectNext(testAuthor())
                .verifyComplete();
    }

    @Test
    void testFindByIds() {
        when(authorRepository.findAllById(any(Publisher.class))).thenReturn(Flux.just(testAuthor()));

        Flux<Author> result = authorService.findByIds(Flux.just("id"));

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }
}
