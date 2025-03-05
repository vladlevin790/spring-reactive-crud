package com.example.springcrud.dao;

import com.example.springcrud.model.Coffee;
import com.example.springcrud.model.OriginDetails;
import com.example.springcrud.repository.CustomCoffeeRepository;
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
            SELECT coffee.id, coffee.name, coffee.count, coffee.origin_details_id,
                   origin_details.id AS origin_id, origin_details.farm, 
                   origin_details.region, origin_details.country
            FROM coffee
            INNER JOIN origin_details ON coffee.origin_details_id = origin_details.id
        """;

        return client.sql(query)
                .map((row, metadata) -> {
                    Coffee coffee = new Coffee();
                    coffee.setId(row.get("id", Long.class));
                    coffee.setName(row.get("name", String.class));
                    coffee.setCount(row.get("count", Long.class));
                    coffee.setOriginDetailsId(row.get("origin_details_id", Long.class));

                    OriginDetails originDetails = new OriginDetails();
                    originDetails.setId(row.get("origin_id", Long.class));
                    originDetails.setFarm(row.get("farm", String.class));
                    originDetails.setRegion(row.get("region", String.class));
                    originDetails.setCountry(row.get("country", String.class));

                    return coffee;
                })
                .all();
    }

    @Override
    public Mono<Coffee> findCoffeeById(Long id) {
        String query = """
            SELECT coffee.id, coffee.name, coffee.count, coffee.origin_details_id,
                   origin_details.id AS origin_id, origin_details.farm, 
                   origin_details.region, origin_details.country
            FROM coffee
            INNER JOIN origin_details ON coffee.origin_details_id = origin_details.id
            WHERE coffee.id = :id
        """;

        return client.sql(query)
                .bind("id", id)
                .map((row, metadata) -> {
                    Coffee coffee = new Coffee();
                    coffee.setId(row.get("id", Long.class));
                    coffee.setName(row.get("name", String.class));
                    coffee.setCount(row.get("count", Long.class));
                    coffee.setOriginDetailsId(row.get("origin_details_id", Long.class));

                    OriginDetails originDetails = new OriginDetails();
                    originDetails.setId(row.get("origin_id", Long.class));
                    originDetails.setFarm(row.get("farm", String.class));
                    originDetails.setRegion(row.get("region", String.class));
                    originDetails.setCountry(row.get("country", String.class));

                    return coffee;
                })
                .one()
                .flatMap(coffee -> coffee.loadTags(tagService).thenReturn(coffee));
    }

    @Override
    public Mono<Coffee> createCoffee(Coffee coffee) {
        String query = """
            INSERT INTO coffee (name, count, origin_details_id) 
            VALUES (:name, :count, :originDetailsId) 
            RETURNING id
        """;

        return client.sql(query)
                .bind("name", coffee.getName())
                .bind("count", coffee.getCount())
                .bind("originDetailsId", coffee.getOriginDetailsId())
                .map((row, metadata) -> row.get("id", Long.class))
                .one()
                .map(id -> {
                    coffee.setId(id);
                    return coffee;
                });
    }

    @Override
    public Mono<Coffee> putCoffee(Long id, Coffee coffee) {
        String query = """
            UPDATE coffee 
            SET name = :name, count = :count, origin_details_id = :originDetailsId 
            WHERE id = :id
        """;

        return client.sql(query)
                .bind("name", coffee.getName())
                .bind("count", coffee.getCount())
                .bind("originDetailsId", coffee.getOriginDetailsId())
                .bind("id", id)
                .fetch()
                .rowsUpdated()
                .flatMap(updatedRows -> updatedRows > 0 ? findCoffeeById(id) : Mono.empty());
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
