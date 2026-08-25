package com.byzquant.fed.repository;

import com.byzquant.fed.entity.FedReleaseCalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FedReleaseCalendarRepository extends JpaRepository<FedReleaseCalendarEntity, Long> {
    // Bugün yayın günü gelmiş ve henüz işlenmemiş tüm kritik serileri listeler
    List<FedReleaseCalendarEntity> findByReleaseDateAndIsProcessedFalse(LocalDate date);
}
