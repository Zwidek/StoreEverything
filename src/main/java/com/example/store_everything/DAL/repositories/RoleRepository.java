package com.example.store_everything.DAL.repositories;

import com.example.store_everything.DAL.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByTitle(String title);

}
