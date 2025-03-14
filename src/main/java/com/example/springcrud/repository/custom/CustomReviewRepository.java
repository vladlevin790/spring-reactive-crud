package com.example.springcrud.repository.custom;

import com.example.springcrud.model.Review;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomReviewRepository {
    Flux<Review> findAllReviews();
    Mono<Review> findReviewById(Long id);
    Mono<Review> createReview(Long coffeeId,Review review);
    Mono<Review> deleteReview(Long id);
}
