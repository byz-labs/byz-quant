package com.byzquant.fed.repository;

import com.byzquant.fed.entity.FedCategoryRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FedCategoryRelationRepository extends JpaRepository<FedCategoryRelationEntity, FedCategoryRelationEntity.RelationId> {
    // Belirli bir kategoriye ait tüm yatay korelasyon bağlarını sorgular
    List<FedCategoryRelationEntity> findByCategoryId(Long categoryId);
}
