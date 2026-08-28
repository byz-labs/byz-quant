package com.byzquant.fed.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name = "macro_observations",
    uniqueConstraints = {
        @UniqueConstraint(name = "uc_indicator_date", columnNames = {"indicator_id", "observation_date"})
    },
    indexes = {
        // Tarihsel rekorları ve analizleri milisaniyede tarayan indeks kilitlendi!
        @Index(name = "idx_indicator_date_desc", columnList = "indicator_id, observation_date DESC")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MacroObservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "indicator_id", nullable = false, length = 50)
    private String indicatorId;

    @Column(name = "observation_date", nullable = false)
    private LocalDate observationDate;

    @Column(name = "observation_value", nullable = false, precision = 15, scale = 4)
    private BigDecimal observationValue;
}
