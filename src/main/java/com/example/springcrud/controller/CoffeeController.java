package com.example.springcrud.controller;

import com.example.springcrud.model.Coffee;
import com.example.springcrud.model.OriginDetails;
import com.example.springcrud.service.CoffeeService;
import com.example.springcrud.service.OriginalDetailsService;
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
@RequestMapping("/coffee")
public class CoffeeController {
    private final CoffeeService service;
    private final OriginalDetailsService detailsService;

    @GetMapping
    public Flux<Coffee> getAllCoffee() {
        return service.getAllCoffee()
                .switchIfEmpty(Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No coffee found")));
    }

    @GetMapping("/{id}/with-details")
    public Mono<Map<String, Object>> getCoffeeWithDetails(@PathVariable Long id) {
        return Mono.zip(
                    service.getCoffeeById(id),
                    detailsService.findByCoffeeId(id).defaultIfEmpty(new OriginDetails())
                )
                .map(tuple -> {
                    Map<String, Object> response = new LinkedHashMap<>();
                    response.put("coffee", tuple.getT1());
                    response.put("details", tuple.getT2());
                    return response;
                });
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