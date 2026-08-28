package com.byzquant.fed.repository;

import com.byzquant.fed.entity.MacroObservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MacroObservationRepository extends JpaRepository<MacroObservation, Long> {

    List<MacroObservation> findTop2ByIndicatorIdOrderByObservationDateDesc(String indicatorId);

    // 🚀 RAM DOSTU YENİ MOTOR: Sadece istenen canavarın geçmişini kronolojik
    // getirir!
    List<MacroObservation> findByIndicatorIdOrderByObservationDateDesc(String indicatorId);

    @Query("SELECT MAX(o.observationValue), MIN(o.observationValue) FROM MacroObservation o " +
            "WHERE o.indicatorId = :indicatorId AND o.observationDate >= :startDate")
    Object[] getHistoricalExtremums(@Param("indicatorId") String indicatorId, @Param("startDate") LocalDate startDate);
}
