package com.example.springcrud.dao;

import com.example.springcrud.model.Tag;
import com.example.springcrud.repository.custom.CustomTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements CustomTagRepository {
    private final DatabaseClient client;

    @Override
    public Flux<Tag> findAllCoffeeTags(Long coffeeId) {
        String query = """
            SELECT t.id AS tag_id, t.name AS tag_name
            FROM tag t
            INNER JOIN coffee_tag ct ON t.id = ct.tag_id
            WHERE ct.coffee_id = :coffeeId
            """;

        return client.sql(query)
                .bind("coffeeId", coffeeId)
                .map((row, metadata) -> {
                    Tag tag = new Tag();
                    tag.setId(row.get("tag_id", Long.class));
                    tag.setName(row.get("tag_name", String.class));
                    return tag;
                })
                .all();
    }

    @Override
    public Mono<Tag> createTag(Tag tag) {
        String query = """
          INSERT INTO tag (name)
          VALUES(:name)
        """;
        return client.sql(query)
                .bind("name",tag.getName())
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(tag);
                    } else  {
                        return Mono.error(new RuntimeException("Failed creation tag"));
                    }
                });
    }

    @Override
    public Mono<Tag> createCoffeeTag(Long coffeeId, Long tagId) {
        String insertQuery = """
            INSERT INTO coffee_tag (coffee_id, tag_id)
            VALUES (:coffee_id, :tag_id)
        """;

        String selectTagQuery = "SELECT * FROM tag WHERE id = :tag_id";

        return client.sql(insertQuery)
                .bind("coffee_id", coffeeId)
                .bind("tag_id", tagId)
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return client.sql(selectTagQuery)
                                .bind("tag_id", tagId)
                                .map((row, meta) ->
                                        new Tag(
                                                row.get("id", Long.class),
                                                row.get("name", String.class)
                                        ))
                                .one();
                    } else {
                        return Mono.error(new RuntimeException("Failed to create association"));
                    }
                })
                .onErrorResume(e ->
                        Mono.error(new RuntimeException("Error creating coffee-tag association: " + e.getMessage()))
                );
    }

    @Override
    public Mono<Tag> putTag(Long id, Tag tag) {
        String query = """
            UPDATE tag 
            SET name = :name 
            WHERE id = :id
        """;
        return client.sql(query)
                .bind("id", id)
                .bind("name", tag.getName())
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(tag);
                    } else {
                        return Mono.error(new RuntimeException("Failed updating tag" ));
                    }
                });
    }

    @Override
    public Mono<Tag> deleteTag(Long id) {
        String query = """
            DELETE FROM tag WHERE id = :id
        """;
        return client.sql(query)
                .bind("id",id)
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(new Tag());
                    } else {
                        return Mono.error(new RuntimeException("Failed to delete tag"));
                    }
                });
    }
}
