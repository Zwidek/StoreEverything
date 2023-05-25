package com.example.store_everything.DAL.repositories;

import com.example.store_everything.DAL.models.AccessType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccessTypeRepository extends JpaRepository<AccessType, UUID> {
    Optional<AccessType> findByTitle(String title);
}
