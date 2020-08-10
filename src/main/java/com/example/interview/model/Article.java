package com.example.interview.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@With
@Data
public class Article {
    @Id
    private String id;
    private String title;
    private String content;
    private LocalDate date;
    @Transient
    private Author author;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String authorId;
}
