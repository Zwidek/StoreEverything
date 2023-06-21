package com.example.store_everything.DAL.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @NotNull
    @Size(min = 3, max = 20)
    @Pattern(regexp = "[A-Z][a-z]*")
    private String name;

    @NotNull
    @Size(min = 3, max = 50)
    @Pattern(regexp = "[A-Z][a-z]*")
    private String surname;

    @NotNull
    @Column(name = "username")
    @Size(min = 3, max = 20)
    @Pattern(regexp = "[a-z0-9]+")
    private String username;

    @NotNull
    @Size(min = 5)
    private String password;

    @NotNull
    @Column(name = "age")
    @Min(18)
    private int age;

    @Email
    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(mappedBy = "sharedWithUsers")
    private List<StoredElement> storedElements;

    @OneToMany(mappedBy = "storedByUser")
    private List<StoredElement> storedByUser;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getTextRoles() {
        StringBuilder sb = new StringBuilder();

        for (Role role : roles) {
            sb.append(role.getTitle());
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        return sb.toString();
    }

    public List<StoredElement> getStoredElements() {
        return storedElements;
    }

    public void setStoredElements(List<StoredElement> storedElements) {
        this.storedElements = storedElements;
    }

    public List<StoredElement> getStoredByUser() {
        return storedByUser;
    }

    public void setStoredByUser(List<StoredElement> storedByUser) {
        this.storedByUser = storedByUser;
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
