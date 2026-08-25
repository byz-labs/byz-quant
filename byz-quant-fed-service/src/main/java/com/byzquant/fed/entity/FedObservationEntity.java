package com.byzquant.fed.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "fed_series_observations")
@IdClass(FedObservationEntity.ObservationId.class) // Composite Key (SeriesId + Date)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FedObservationEntity {

    @Id
    @Column(name = "series_id")
    private String seriesId; // Örn: 'FEDFUNDS'

    @Id
    @Column(name = "date")
    private LocalDate date; // Verinin ait olduğu gün (Örn: 2026-08-25)

    @Column(name = "value")
    private String value; // FED bazen gelmeyen veriler için '.' döner, bu yüzden String tutup iş mantığında temizleyeceğiz

    // Lombok ile tek satıra inen Composite ID yapısı
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObservationId implements Serializable {
        private String seriesId;
        private LocalDate date;
    }
}
