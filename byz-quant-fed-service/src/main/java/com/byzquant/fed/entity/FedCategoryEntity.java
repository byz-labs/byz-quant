package com.byzquant.fed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fed_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FedCategoryEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    // Setter'ı el ile özelleştirerek 0 -> 0 döngüsel bağımlılık hatasını burada kökten engelliyoruz
    @Column(name = "parent_id")
    @Setter(AccessLevel.NONE) 
    private Long parentId;

    // Akıllı Kök (Root) Engelleyici
    public void setParentId(Long parentId) {
        if (parentId != null && parentId.equals(this.id)) {
            this.parentId = null; // id=0 ve parent_id=0 ise PostgreSQL hatası vermemesi için null yap
        } else {
            this.parentId = parentId;
        }
    }
}
