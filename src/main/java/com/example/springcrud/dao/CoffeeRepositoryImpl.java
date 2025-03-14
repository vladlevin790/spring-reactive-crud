package com.example.springcrud.dao;

import com.example.springcrud.model.Coffee;
import com.example.springcrud.repository.custom.CustomCoffeeRepository;
import com.example.springcrud.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CoffeeRepositoryImpl implements CustomCoffeeRepository {
    private final DatabaseClient client;
    private final TagService tagService;

    @Override
    public Flux<Coffee> findAllCoffees() {
        String query = """
            SELECT id, name, count FROM coffee
        """;

        return client.sql(query)
                .map((row, metadata) -> {
                    Coffee coffee = new Coffee();
                    coffee.setId(row.get("id", Long.class));
                    coffee.setName(row.get("name", String.class));
                    coffee.setCount(row.get("count", Long.class));

                    return coffee;
                })
                .all();
    }

    @Override
    public Mono<Coffee> findCoffeeById(Long id) {
        String query = """
            SELECT * 
            FROM coffee where id = :id;
        """;

        return client.sql(query)
                .bind("id", id)
                .map((row, metadata) -> {
                    Coffee coffee = new Coffee();
                    coffee.setId(row.get("id", Long.class));
                    coffee.setName(row.get("name", String.class));
                    coffee.setCount(row.get("count", Long.class));
                    return coffee;
                })
                .one()
                .flatMap(coffee -> coffee.loadTags(tagService).thenReturn(coffee));
    }

    @Override
    public Mono<Coffee> createCoffee(Coffee coffee) {
        String query = """
            INSERT INTO coffee (name, count) 
            VALUES (:name, :count)
        """;

       return client.sql(query)
               .bind("name", coffee.getName())
               .bind("count", coffee.getCount())
               .fetch()
               .rowsUpdated()
               .flatMap(rowsUpdated -> {
                   if (rowsUpdated > 0) {
                       return Mono.just(coffee);
                   } else {
                       return Mono.error(new RuntimeException("Failed to insert coffee"));
                   }
               });
    }

    @Override
    public Mono<Coffee> putCoffee(Long id, Coffee coffee) {
       String query = """
           UPDATE coffee 
           SET name = :name, count = :count 
           WHERE id = :id
       """;

       return client.sql(query)
               .bind("id", id)
               .bind("name", coffee.getName())
               .bind("count", coffee.getCount())
               .fetch()
               .rowsUpdated()
               .flatMap(rowsUpdated -> {
                   if (rowsUpdated > 0) {
                       return  Mono.just(coffee);
                   } else {
                       return  Mono.error(new RuntimeException("Failed to update coffee with id"));
                   }
               });
    }

    @Override
    public Mono<Void> deleteCoffee(Long id) {
        String query = "DELETE FROM coffee WHERE id = :id";

        return client.sql(query)
                .bind("id", id)
                .fetch()
                .rowsUpdated()
                .then();
    }
}
