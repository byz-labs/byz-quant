package com.byzquant.fed.adapters.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fed_categories")
public class FedCategoryEntity {

    @Id
    private Long id;
    private String name;
    private Long parentId;

    // JPA için zorunlu boş constructor
    protected FedCategoryEntity() {}

    public FedCategoryEntity(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public Long getParentId() { return parentId; }
}
