package com.example.store_everything.DAL.repositories;

import com.example.store_everything.DAL.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, UUID> {

    Optional<ApplicationUser> findByUsername(String username);

}
