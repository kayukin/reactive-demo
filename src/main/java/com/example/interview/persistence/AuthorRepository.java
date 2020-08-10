package com.example.interview.persistence;

import com.example.interview.model.Author;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
}
