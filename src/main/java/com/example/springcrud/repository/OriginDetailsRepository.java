package com.example.springcrud.repository;

import com.example.springcrud.repository.custom.CustomOriginDetailsRepository;
import org.springframework.data.repository.CrudRepository;

public interface OriginDetailsRepository extends CrudRepository<OriginDetailsRepository,Long>, CustomOriginDetailsRepository {
}
