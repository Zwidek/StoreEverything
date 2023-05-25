package com.example.store_everything.DAL.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;
import java.util.UUID;

public class StoredElementDTO {

    private UUID id;
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 20, message = "Title length should be between 3 and 20 characters")
    private String title;
    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 500, message = "Description length should be between 5 and 500 characters")
    private String description;
    @URL
    private String link;
    @URL
    private String sharedLink;
    private String accessType;
    private String category;
    private List<String> sharedWithUsers;

    public StoredElementDTO(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSharedLink() {
        return sharedLink;
    }

    public void setSharedLink(String sharedLink) {
        this.sharedLink = sharedLink;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public List<String> getSharedWithUsers() {
        return sharedWithUsers;
    }

    public void setSharedWithUsers(List<String> sharedWithUsers) {
        this.sharedWithUsers = sharedWithUsers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
