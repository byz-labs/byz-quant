package com.byzquant.fed.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FedCategoryRelationRepository extends JpaRepository<FedCategoryRelationEntity, FedCategoryRelationEntity.RelationId> {
    // Belirli bir kategorinin ilişkili olduğu tüm kategorilerin ID listesini döner
    List<FedCategoryRelationEntity> findByCategoryId(Long categoryId);
}
