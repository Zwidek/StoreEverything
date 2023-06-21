package com.example.store_everything.DAL.repositories;

import com.example.store_everything.DAL.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByTitle(String title);

}
