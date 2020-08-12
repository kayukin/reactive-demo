package com.example.interview;

import com.example.interview.model.Article;
import com.example.interview.model.Author;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import java.time.LocalDate;

public class TestUtil {
    public static Author testAuthor() {
        return new Author("authorId", "test", LocalDate.now().minusYears(40), 2L);
    }

    public static Article testArticle() {
        Lorem lorem = LoremIpsum.getInstance();
        return new Article(null, lorem.getTitle(4), lorem.getParagraphs(2, 3),
                LocalDate.now(), testAuthor(), "authorId");
    }
}
