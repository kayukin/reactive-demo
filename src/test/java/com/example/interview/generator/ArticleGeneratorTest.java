package com.example.interview.generator;

import com.example.interview.model.Article;
import com.example.interview.model.Author;
import com.example.interview.service.ArticleService;
import com.example.interview.service.AuthorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleGeneratorTest {
    private ArticleGenerator articleGenerator;

    @Mock
    private ArticleService articleService;
    @Mock
    private AuthorService authorService;
    @Captor
    private ArgumentCaptor<Article> articleCaptor;

    @BeforeEach
    void setUp() {
        articleGenerator = new ArticleGenerator(articleService, authorService);
    }

    @Test
    void testGenerateArticle() {
        when(authorService.findAll()).thenReturn(Flux.just(new Author().withId("test")));
        when(articleService.addNew(any())).thenReturn(Mono.empty());

        articleGenerator.generateArticle();
        verify(articleService).addNew(articleCaptor.capture());

        Assertions.assertEquals("test", articleCaptor.getValue().getAuthorId());
    }
}
