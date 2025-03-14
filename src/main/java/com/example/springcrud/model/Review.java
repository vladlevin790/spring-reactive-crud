package com.example.springcrud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    private Long id;
    private String comment;
    private int rating;
    private Long coffeeId;
}
