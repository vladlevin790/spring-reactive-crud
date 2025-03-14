package com.example.springcrud.repository;

import com.example.springcrud.model.Review;
import com.example.springcrud.repository.custom.CustomReviewRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review,Long>, CustomReviewRepository {
}
