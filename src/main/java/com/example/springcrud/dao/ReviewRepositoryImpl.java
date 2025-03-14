package com.example.springcrud.dao;

import com.example.springcrud.model.Review;
import com.example.springcrud.repository.custom.CustomReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements CustomReviewRepository {
    private final DatabaseClient client;
    @Override
    public Flux<Review> findAllReviews() {
        String query = """
            SELECT * FROM review;
        """;
        return client.sql(query)
                .map((row,metadata) -> {
                    Review review = new Review();
                    review.setId(row.get("id",Long.class));
                    review.setComment(row.get("comment",String.class));
                    review.setRating(row.get("rating",Integer.class));
                    review.setCoffeeId(row.get("coffe_id",Long.class));
                    return review;
                })
                .all();
    }

    @Override
    public Mono<Review> findReviewById(Long id) {
        String query = """
            SELECT r.*, c.id AS coffee_id, c.name AS coffee_name, c.count AS coffee_count 
            FROM review r
            LEFT JOIN coffee c ON r.coffe_id = c.id
            WHERE r.id = :id
        """;

        return client.sql(query)
                .bind("id", id)
                .map((row, meta) -> {
                    Review review = new Review();
                    review.setId(row.get("id", Long.class));
                    review.setComment(row.get("comment", String.class));
                    review.setRating(row.get("rating", Integer.class));
                    review.setCoffeeId(row.get("coffe_id",Long.class));
                    return review;
                })
                .one()
                .onErrorResume(e -> Mono.error(
                        new RuntimeException("Review not found with id: " + id)
                ));
    }

    @Override
    public Mono<Review> createReview(Long coffeeId, Review review) {
        String insertQuery = """
        INSERT INTO review (comment, rating, coffe_id)
        VALUES (:comment, :rating, :coffe_id)
    """;

        String selectQuery = """
        SELECT * 
        FROM review 
        WHERE id = LAST_INSERT_ID()
    """;

        return client.sql(insertQuery)
                .bind("comment", review.getComment())
                .bind("rating", review.getRating())
                .bind("coffe_id", coffeeId)
                .fetch()
                .rowsUpdated()
                .flatMap(rows -> client.sql(selectQuery)
                        .map((row, meta) -> {
                            Review created = new Review();
                            created.setId(row.get("id", Long.class));
                            created.setComment(row.get("comment", String.class));
                            created.setRating(row.get("rating", Integer.class));
                            created.setCoffeeId(row.get("coffe_id", Long.class));
                            return created;
                        })
                        .one()
                );
    }

    @Override
    public Mono<Review> deleteReview(Long id) {
        String query = """
            DELETE FROM review 
            WHERE id = :id
            RETURNING *
        """;

        return client.sql(query)
                .bind("id", id)
                .map((row, meta) -> {
                    Review deleted = new Review();
                    deleted.setId(row.get("id", Long.class));
                    deleted.setComment(row.get("comment", String.class));
                    deleted.setRating(row.get("rating", Integer.class));
                    return deleted;
                })
                .one()
                .onErrorResume(e -> Mono.error(
                        new RuntimeException("Failed to delete review with id: " + id)
                ));
    }
}
