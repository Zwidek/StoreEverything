package com.example.store_everything.DAL.repositories;

import com.example.store_everything.DAL.models.AccessType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTypeRepository extends JpaRepository<AccessType, Long> {
    Optional<AccessType> findByTitle(String title);
}
