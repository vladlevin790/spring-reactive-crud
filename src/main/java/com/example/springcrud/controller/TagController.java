package com.example.springcrud.controller;

import com.example.springcrud.model.Tag;
import com.example.springcrud.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {
    private final TagService service;

    @PostMapping
    public Mono<ResponseEntity<String>> createTag(@RequestBody Tag tag) {
        return service.createTag(tag)
                .map(message -> ResponseEntity.ok(message))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error")));
    }

    @PostMapping("/{id}/coffee/{coffeeId}")
    public Mono<ResponseEntity<String>> createCoffeeTag(@PathVariable Long id, @PathVariable Long coffeeId) {
        return service.createCoffeeTag(coffeeId,id)
                .map(message -> ResponseEntity.ok(message))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error")));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<String>> putTag(@PathVariable Long id, @RequestBody Tag tag) {
        return service.putTag(id, tag)
                .map(message -> ResponseEntity.ok(message))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error")));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteCoffeeTag(@PathVariable Long id) {
        return service.deleteTag(id)
                .map(message -> ResponseEntity.ok(message))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error")));
    }

}
