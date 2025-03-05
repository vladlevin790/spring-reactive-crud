package com.example.springcrud.service;

import com.example.springcrud.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final DatabaseClient databaseClient;

    public Mono<List<Tag>> findAllCoffeeTags(long coffeeId) {
        String query = """
            SELECT t.id AS tag_id, t.name AS tag_name
            FROM tag t
            INNER JOIN coffee_tag ct ON t.id = ct.tag_id
            WHERE ct.coffee_id = :coffeeId
            """;

        return databaseClient.sql(query)
                .bind("coffeeId", coffeeId)
                .map((row, metadata) -> {
                    Tag tag = new Tag();
                    tag.setId(row.get("tag_id", Long.class));
                    tag.setName(row.get("tag_name", String.class));
                    return tag;
                })
                .all()
                .collectList();
    }
}