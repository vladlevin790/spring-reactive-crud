package com.example.springcrud.service;

import com.example.springcrud.model.Tag;
import com.example.springcrud.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository repository;

    public Mono<List<Tag>> findAllCoffeeTags(long coffeeId) {
        return repository.findAllCoffeeTags(coffeeId)
                .collectList();
    }

    public Mono<String> createTag(Tag tag) {
        return repository.createTag(tag)
                .thenReturn("Created")
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }

    public Mono<String> createCoffeeTag(Long coffeeId, Long id) {
        return repository.createCoffeeTag(coffeeId,id)
                .thenReturn("Created")
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }

    public Mono<String> putTag (Long id, Tag tag) {
        return repository.putTag(id, tag)
                .thenReturn("Updated")
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }

    public Mono<String> deleteTag(Long id) {
        return repository.deleteTag(id)
                .thenReturn("Deleted")
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }
}