package com.byzquant.fed.repository;

import com.byzquant.fed.entity.FedSeriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FedSeriesRepository extends JpaRepository<FedSeriesEntity, String> {
    // Belirli bir kategori altındaki tüm serileri sorgular
    List<FedSeriesEntity> findByCategoryId(Long categoryId);
}
