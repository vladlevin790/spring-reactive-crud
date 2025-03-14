package com.example.springcrud.service;

import com.example.springcrud.model.Review;
import com.example.springcrud.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository repository;

    public Flux<Review> getAllCoffees() {
        return repository.findAllReviews();
    }

    public Mono<Review> getReviewById(Long id) {
        return repository.findReviewById(id);
    }

    public Mono<String> createReview(Long coffeeId, Review review) {
        return repository.createReview(coffeeId,review)
                .thenReturn("Created")
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }

    public Mono<String> deleteReview(Long id) {
        return repository.deleteReview(id)
                .thenReturn("Deleted")
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }

}
