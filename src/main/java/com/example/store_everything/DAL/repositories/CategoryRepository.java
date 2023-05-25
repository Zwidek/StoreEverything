package com.example.store_everything.DAL.repositories;

import com.example.store_everything.DAL.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findByTitle(String title);

}
