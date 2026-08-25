package com.byzquant.fed.repository;

import com.byzquant.fed.entity.FedObservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FedObservationRepository extends JpaRepository<FedObservationEntity, FedObservationEntity.ObservationId> {
    // Belirli bir serinin tüm tarihsel fiyat/değer satırlarını kronolojik sırada getirir
    List<FedObservationEntity> findBySeriesIdOrderByDateAsc(String seriesId);
}
