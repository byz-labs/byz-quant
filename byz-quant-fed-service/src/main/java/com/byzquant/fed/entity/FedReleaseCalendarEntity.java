package com.byzquant.fed.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "fed_release_calendar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FedReleaseCalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "series_id", nullable = false)
    private String seriesId; // Örn: 'CPIAUCSL'

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate; // Yayın günü (Örn: 2026-09-11)

    @Column(name = "release_time")
    private LocalTime releaseTime; // Nokta atışı saat (Örn: 08:30:00)

    @Column(name = "is_processed", nullable = false)
    private Boolean isProcessed; // Veriyi çekip başarıyla ambarımıza gömdük mü?
}
