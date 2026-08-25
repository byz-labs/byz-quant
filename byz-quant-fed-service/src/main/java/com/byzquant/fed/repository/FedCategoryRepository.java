package com.byzquant.fed.repository;

import com.byzquant.fed.entity.FedCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FedCategoryRepository extends JpaRepository<FedCategoryEntity, Long> {
}
