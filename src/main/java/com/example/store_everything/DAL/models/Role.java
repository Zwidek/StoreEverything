package com.example.store_everything.DAL.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @Column(unique = true)
    private String title;

    public Role(){

    }

    public Role(String title) {
        this.title = title;
    }

    @ManyToMany(mappedBy = "roles")
    private List<ApplicationUser> users;

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

    public List<ApplicationUser> getUsers() {
        return users;
    }

    public void setUsers(List<ApplicationUser> users) {
        this.users = users;
    }
}
