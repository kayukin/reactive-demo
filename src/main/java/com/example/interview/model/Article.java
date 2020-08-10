package com.example.interview.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.time.LocalDate;

@Data
public class Article {
    @Id
    private String id;
    private String title;
    private String content;
    private LocalDate date;
    @Transient
    private Author author;
    private String authorId;
}
