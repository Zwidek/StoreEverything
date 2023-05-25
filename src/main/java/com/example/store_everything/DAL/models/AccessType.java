package com.example.store_everything.DAL.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "access_type")
public class AccessType {


    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    private String title;

    @OneToMany(mappedBy = "accessType", cascade = CascadeType.ALL)
    private List<StoredElement> storedElementList;

    public AccessType(){}
    public AccessType(String title){
        this.title = title;
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
