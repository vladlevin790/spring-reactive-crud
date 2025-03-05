package com.example.springcrud.service;

import com.example.springcrud.model.Coffee;
import com.example.springcrud.repository.CoffeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CoffeeService {
    private final CoffeeRepository repository;

    public Flux<Coffee> getAllCoffee() {
        return repository.findAllCoffees();
    }

    public Mono<Coffee> getCoffeeById(Long id) {
        return repository.findCoffeeById(id);
    }

    public Mono<String> postCoffee(Coffee coffee) {
        return repository.createCoffee(coffee)
                .thenReturn("Created")
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }

    public Mono<String> putCoffee(Coffee coffee, Long id) {
        return repository.putCoffee(id, coffee)
                .thenReturn("Updated")
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }

    public Mono<String> deleteCoffee(Long id) {
        return repository.deleteCoffee(id)
                .thenReturn("Deleted")
                .onErrorReturn("Error");
    }
}