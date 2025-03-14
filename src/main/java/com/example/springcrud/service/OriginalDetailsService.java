package com.example.springcrud.service;

import com.example.springcrud.model.OriginDetails;
import com.example.springcrud.repository.OriginDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OriginalDetailsService {
    private final OriginDetailsRepository repository;

    public Mono<OriginDetails> findByCoffeeId(Long id) {
        return repository.findByCoffeeId(id);
    }

    public Mono<String> postOriginDetails(OriginDetails originDetails, Long coffeeId){
        return repository.createOriginDetails(originDetails,coffeeId)
                .thenReturn("Created")
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }

    public Mono<String> putOriginDetails(Long id, Long coffeeId, OriginDetails originDetails) {
        return repository.putOiriginDetails(originDetails,id,coffeeId)
                .thenReturn("Updated")
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }

    public Mono<String> deleteOriginDetails(Long id) {
        return repository.deleteOriginDetails(id)
                .thenReturn("Deleted")
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));
    }
}
