package com.example.springcrud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OriginDetails {
    @Id
    private Long id;
    private String country;
    private String region;
    private String farm;

    private Long coffee_id;
}
