package com.example.springcrud.repository.custom;

import com.example.springcrud.model.OriginDetails;
import reactor.core.publisher.Mono;

public interface CustomOriginDetailsRepository {
    Mono<OriginDetails> findByCoffeeId(Long id);
    Mono<OriginDetails> createOriginDetails(OriginDetails originDetails, Long coffeId);
    Mono<OriginDetails> putOiriginDetails(OriginDetails originDetails, Long id, Long coffeeId);
    Mono<OriginDetails> deleteOriginDetails(Long id);
}
