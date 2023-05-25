package com.example.store_everything.DAL.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ApplicationUserDTO {


    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 20, message = "Name length should be between 3 and 20 characters")
    @Pattern(regexp = "[A-Z][a-z]*", message = "Name should be capitalize")
    private String name;
    @NotBlank(message = "Surname is required")
    @Size(min = 3, max = 50, message = "Surname length should be between 3 and 50 characters")
    @Pattern(regexp = "[A-Z][a-z]*", message = "Surname should be capitalize")
    private String surname;
    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password length should be bigger than 5 characters")
    private String password;
    @NotBlank(message = "Repeat password is required")
    private String matchingPassword;
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username length should be between 3 and 20 characters")
    @Pattern(regexp = "[a-z]*", message = "Username should be lowercase")
    private String username;
    @Min(value = 18, message = "Your age should be equal to or greater than 18")
    private int age;
    @Email
    private String email;

    public ApplicationUserDTO(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
