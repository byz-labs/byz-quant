package com.byzquant.fed;

import com.byzquant.fed.adapters.persistence.FedCategoryRelationEntity;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class FedCategoryRelationEntityTest {

    @Test
    void shouldVerifyEntityAndCompositeIdBehaviors() {
        // 1. Entity Getters Kontrolü
        FedCategoryRelationEntity entity = new FedCategoryRelationEntity(125L, 10L);
        assertThat(entity.getCategoryId()).isEqualTo(125L);
        assertThat(entity.getRelatedCategoryId()).isEqualTo(10L);

        // 2. Composite ID (RelationId) Equals ve HashCode Kontrolü (Sonar Barajı Patlatan Alan)
        FedCategoryRelationEntity.RelationId id1 = new FedCategoryRelationEntity.RelationId(125L, 10L);
        FedCategoryRelationEntity.RelationId id2 = new FedCategoryRelationEntity.RelationId(125L, 10L);
        FedCategoryRelationEntity.RelationId id3 = new FedCategoryRelationEntity.RelationId(999L, 999L);

        assertThat(id1).isEqualTo(id2);
        assertThat(id1).isNotEqualTo(id3);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        assertThat(id1).isNotEqualTo(new Object());
    }
}
