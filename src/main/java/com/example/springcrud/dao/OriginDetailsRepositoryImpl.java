package com.example.springcrud.dao;

import com.example.springcrud.model.OriginDetails;
import com.example.springcrud.repository.custom.CustomOriginDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class OriginDetailsRepositoryImpl implements CustomOriginDetailsRepository {
    private final DatabaseClient client;

    @Override
    public Mono<OriginDetails> findByCoffeeId(Long id) {
        String query = """
            SELECT * FROM origin_details WHERE coffee_id = :id
        """;
        return client.sql(query)
                .bind("id", id)
                .map((row, metadata) -> {
                    OriginDetails originDetails = new OriginDetails();
                    originDetails.setId(row.get("id", Long.class));
                    originDetails.setRegion(row.get("region", String.class));
                    originDetails.setCountry(row.get("country", String.class));
                    originDetails.setFarm(row.get("farm", String.class));
                    originDetails.setCoffee_id(row.get("coffee_id", Long.class));
                    return originDetails;
                }).one();
    }

    @Override
    public Mono<OriginDetails> createOriginDetails(OriginDetails originDetails, Long coffeeId) {
        String query = """
            INSERT INTO origin_details (region, country, farm, coffee_id) 
            VALUES (:region, :country, :farm, :coffee_id)
        """;
        return client.sql(query)
                .bind("region", originDetails.getRegion())
                .bind("country", originDetails.getCountry())
                .bind("farm", originDetails.getFarm())
                .bind("coffee_id", coffeeId)
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(originDetails);
                    } else {
                        return Mono.error(new RuntimeException("Failed to insert origin details"));
                    }
                });
    }

    @Override
    public Mono<OriginDetails> putOiriginDetails(OriginDetails originDetails, Long id, Long coffeeId) {
        String query = """
            UPDATE origin_details 
            SET region = :region, country = :country, farm = :farm, coffee_id = :coffee_id 
            WHERE id = :id
        """;
        return client.sql(query)
                .bind("region", originDetails.getRegion())
                .bind("country", originDetails.getCountry())
                .bind("farm", originDetails.getFarm())
                .bind("coffee_id", coffeeId)
                .bind("id", id)
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(originDetails);
                    } else {
                        return Mono.error(new RuntimeException("Failed to update origin details"));
                    }
                });
    }

    @Override
    public Mono<OriginDetails> deleteOriginDetails(Long id) {
        String query = """
            DELETE FROM origin_details WHERE id = :id
        """;
        return client.sql(query)
                .bind("id", id)
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(new OriginDetails());
                    } else {
                        return Mono.error(new RuntimeException("Failed to delete origin details"));
                    }
                });
    }
}

