package com.example.springcrud.controller;

import com.example.springcrud.model.Coffee;
import com.example.springcrud.service.CoffeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequiredArgsConstructor
@RequestMapping("/coffee")
public class CoffeeController {
    private final CoffeeService service;

    @GetMapping
    public Flux<Coffee> getAllCoffee() {
        return service.getAllCoffee();
    }

    @PostMapping
    public Mono<ResponseEntity<String>> createCoffee(@RequestBody Coffee coffee) {
        return service.postCoffee(coffee)
                .map(message -> ResponseEntity.ok(message))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error")));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Coffee>> getCoffeeById(@PathVariable Long id) {
        return service.getCoffeeById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<String>> putCoffee(@RequestBody Coffee coffee, @PathVariable Long id) {
        return service.putCoffee(coffee, id)
                .map(message -> {
                    if (message.equals("Coffee not found")) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
                    }
                    return ResponseEntity.ok(message);
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error"));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteById(@PathVariable Long id) {
        return service.deleteCoffee(id)
                .map(message -> {
                    if (message.equals("Coffee not found")) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
                    }
                    return ResponseEntity.ok(message);
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error"));
    }
}