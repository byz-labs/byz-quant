package com.byzquant.fed.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "fed_category_relations")
@IdClass(FedCategoryRelationEntity.RelationId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FedCategoryRelationEntity {

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Id
    @Column(name = "related_category_id")
    private Long relatedCategoryId;

    // Lombok sayesinde o eski 50 satırlık karmaşık equals/hashCode metotları tek satıra düştü
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelationId implements Serializable {
        private Long categoryId;
        private Long relatedCategoryId;
    }
}
