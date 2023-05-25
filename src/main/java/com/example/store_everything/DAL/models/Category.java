package com.example.store_everything.DAL.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category")
public class Category {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @NotNull(message = "Title is required")
    @Size(min=3, max=20, message = "Title length should be between 3 and 20 characters")
    @Pattern(regexp = "[a-z]+", message = "Title should be lowercase and can't have special characters")
    private String title;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<StoredElement> storedElementList;

    public Category() {
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<StoredElement> getStoredElementList() {
        return storedElementList;
    }

    public void setStoredElementList(List<StoredElement> storedElementList) {
        this.storedElementList = storedElementList;
    }
}
