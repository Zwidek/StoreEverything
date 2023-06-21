package com.example.store_everything.DAL.repositories;

import com.example.store_everything.DAL.models.StoredElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoredElementRepository extends JpaRepository<StoredElement, Long> {

    List<StoredElement> findByAccessType_Title(String accessTypeTitle);
    Optional<StoredElement> findBySharedLink(String link);

}
