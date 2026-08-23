package com.byzquant.fed.adapters.persistence;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "fed_category_relations")
@IdClass(FedCategoryRelationEntity.RelationId.class) // Çift primary key yönetimi
public class FedCategoryRelationEntity {

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Id
    @Column(name = "related_category_id")
    private Long relatedCategoryId;

    public FedCategoryRelationEntity() {}

    public FedCategoryRelationEntity(Long categoryId, Long relatedCategoryId) {
        this.categoryId = categoryId;
        this.relatedCategoryId = relatedCategoryId;
    }

    public Long getCategoryId() { return categoryId; }
    public Long getRelatedCategoryId() { return relatedCategoryId; }

    // JPA için composite key sınıfı
    public static class RelationId implements Serializable {
        private Long categoryId;
        private Long relatedCategoryId;

        public RelationId() {}
        public RelationId(Long categoryId, Long relatedCategoryId) {
            this.categoryId = categoryId;
            this.relatedCategoryId = relatedCategoryId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RelationId that)) return false;
            return Objects.equals(categoryId, that.categoryId) && Objects.equals(relatedCategoryId, that.relatedCategoryId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(categoryId, relatedCategoryId);
        }
    }
}
