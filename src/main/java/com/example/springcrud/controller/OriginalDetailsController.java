package com.example.springcrud.controller;

import com.example.springcrud.model.OriginDetails;
import com.example.springcrud.service.OriginalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/details")
public class OriginalDetailsController {
    private final OriginalDetailsService service;

    @GetMapping("/coffee/{id}")
    public Mono<OriginDetails> getOriginDetailsCoffee(@PathVariable Long id) {
        return service.findByCoffeeId(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"no info found")));
    }

    @PostMapping("/{coffeeId}")
    public Mono<ResponseEntity<String>> postOriginalDetails(@PathVariable Long coffeeId, @RequestBody OriginDetails details) {
        return service.postOriginDetails(details,coffeeId)
                .map(message -> ResponseEntity.ok(message))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error")));
    }

    @PutMapping("/{id}/{coffeeId}")
    public Mono<ResponseEntity<String>> putOriginDetails(@PathVariable Long id, @PathVariable Long coffeeId, @RequestBody OriginDetails details) {
        return service.putOriginDetails(id,coffeeId,details)
                .map(message -> ResponseEntity.ok(message))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error")));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteOriginDetails(@PathVariable Long id) {
        return service.deleteOriginDetails(id)
                .map(message -> ResponseEntity.ok(message))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error")));
    }

}
