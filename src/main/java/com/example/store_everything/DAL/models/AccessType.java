package com.example.store_everything.DAL.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "access_type")
public class AccessType {


    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String title;

    @OneToMany(mappedBy = "accessType", cascade = CascadeType.ALL)
    private List<StoredElement> storedElementList;

    public AccessType(){}
    public AccessType(String title){
        this.title = title;
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
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
