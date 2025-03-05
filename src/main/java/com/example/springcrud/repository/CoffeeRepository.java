package com.example.springcrud.repository;

import com.example.springcrud.model.Coffee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CoffeeRepository extends ReactiveCrudRepository<Coffee, Long>,CustomCoffeeRepository { }
