package com.example.springcrud.repository.custom;

import com.example.springcrud.model.Tag;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Repository
public interface CustomTagRepository {
    Flux<Tag> findAllCoffeeTags(Long coffeeId);
    Mono<Tag> createTag(Tag tag);
    Mono<Tag> createCoffeeTag(Long coffeeId,Long tagId);
    Mono<Tag> putTag(Long id, Tag tag);
    Mono<Tag> deleteTag(Long id);
}
