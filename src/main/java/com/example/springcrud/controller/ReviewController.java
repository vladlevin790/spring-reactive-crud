package com.example.springcrud.controller;

import com.example.springcrud.model.Coffee;
import com.example.springcrud.model.Review;
import com.example.springcrud.service.CoffeeService;
import com.example.springcrud.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService service;
    private final CoffeeService coffeeService;

    @GetMapping
    public Flux<Review> getALlReview() {
        return service.getAllCoffees()
                .switchIfEmpty(Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No reviews found")));
    }

    @GetMapping("/{id}")
    public Mono<Map<String, Object>> getReviewById(@PathVariable Long id) {
        return service.getReviewById(id)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found")
                ))
                .flatMap(review ->
                        Mono.zip(
                                Mono.just(review),
                                coffeeService.getCoffeeById(review.getCoffeeId())
                                        .defaultIfEmpty(new Coffee())
                        )
                )
                .map(tuple -> {
                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("review", tuple.getT1());
                    response.put("coffee", tuple.getT2());
                    return response;
                });
    }

    @PostMapping("/{coffeeId}")
    public Mono<ResponseEntity<String>> createReview(@PathVariable Long coffeeId, @RequestBody Review review) {
        System.out.println(review.getComment());
        System.out.println(review.getRating());
        System.out.println(coffeeId);

        return service.createReview(coffeeId,review)
                .map( message -> ResponseEntity.ok(message))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error")));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteReview(@PathVariable Long id) {
        return service.deleteReview(id)
                .map( message -> ResponseEntity.ok(message))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found")));
    }
}
