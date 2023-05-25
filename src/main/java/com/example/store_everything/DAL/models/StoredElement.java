package com.example.store_everything.DAL.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stored_element")
public class StoredElement {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @NotNull
    @Size(min = 3, max = 20)
    private String title;

    @URL
    private String link;

    @NotNull
    @Size(min = 5, max = 500)
    private String description;

    @NotNull
    private Date creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "access_type_id")
    private AccessType accessType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private ApplicationUser storedByUser;
    @URL
    private String sharedLink;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "users_elements",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "stored_element_id", referencedColumnName = "id")})
    private List<ApplicationUser> sharedWithUsers = new ArrayList<>();

    public StoredElement(){

    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getSharedLink() {
        return sharedLink;
    }

    public void setSharedLink(String sharedLink) {
        this.sharedLink = sharedLink;
    }

    public List<ApplicationUser> getSharedWithUsers() {
        return sharedWithUsers;
    }

    public void setSharedWithUsers(List<ApplicationUser> sharedWithUsers) {
        this.sharedWithUsers = sharedWithUsers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ApplicationUser getStoredByUser() {
        return storedByUser;
    }

    public void setStoredByUser(ApplicationUser storedByUser) {
        this.storedByUser = storedByUser;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public String getFormattedDate(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(creationDate).toString();
    }

    @PrePersist
    public void prePersist() {
        creationDate = new Date();
    }
}
