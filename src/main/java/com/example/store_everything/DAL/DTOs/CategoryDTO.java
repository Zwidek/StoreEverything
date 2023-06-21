package com.example.store_everything.DAL.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class CategoryDTO {
    private Long id;
    @NotBlank(message = "Title is required")
    @Size(min=3, max=20, message = "Title length should be between 3 and 20 characters")
    @Pattern(regexp = "[a-z]+", message = "Title should be lowercase and can't have special characters")
    private String title;

    public CategoryDTO() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
