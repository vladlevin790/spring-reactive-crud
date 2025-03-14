package com.example.springcrud.repository.custom;

import com.example.springcrud.model.Coffee;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomCoffeeRepository {
    Flux<Coffee> findAllCoffees();
    Mono<Coffee> findCoffeeById(Long id);
    Mono<Coffee> createCoffee(Coffee coffee);
    Mono<Coffee> putCoffee(Long id,Coffee coffee);
    Mono<Void> deleteCoffee(Long id);
}
