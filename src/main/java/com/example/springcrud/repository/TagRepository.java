package com.example.springcrud.repository;

import com.example.springcrud.model.Tag;
import com.example.springcrud.repository.custom.CustomTagRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<Tag,Long>, CustomTagRepository {
}
