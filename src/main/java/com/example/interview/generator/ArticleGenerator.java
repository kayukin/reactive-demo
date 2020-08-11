package com.example.interview.generator;

import com.example.interview.model.Article;
import com.example.interview.model.Author;
import com.example.interview.service.ArticleService;
import com.example.interview.service.AuthorService;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ArticleGenerator {
    private final Lorem lorem = LoremIpsum.getInstance();
    private final ArticleService articleService;
    private final AuthorService authorService;
    private final Random random;

    public ArticleGenerator(ArticleService articleService, AuthorService authorService) {
        this.articleService = articleService;
        this.authorService = authorService;
        random = new Random();
    }

    @Scheduled(fixedDelay = 20000)
    public void generateArticle() {
        List<String> ids = authorService.findAll()
                .map(Author::getId)
                .toStream()
                .collect(Collectors.toList());

        String authorId = ids.get(random.nextInt(ids.size()));

        Article article = new Article();
        article.setTitle(lorem.getTitle(5, 10));
        article.setContent(lorem.getParagraphs(4, 7));
        article.setAuthorId(authorId);

        log.info("Generating article for author: {}", authorId);
        articleService.addNew(article)
                .subscribe();
    }
}
