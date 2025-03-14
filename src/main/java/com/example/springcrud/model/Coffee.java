package com.example.springcrud.model;

import com.example.springcrud.service.TagService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coffee {
    @Id
    private Long id;

    private String name;

    private Long count;

    private List<Tag> coffeTag = new ArrayList<>();

    public Mono<List<Tag>> loadTags(TagService tagService) {
        return tagService.findAllCoffeeTags(this.id)
                .doOnNext(tags -> this.coffeTag = tags);
    }
}
