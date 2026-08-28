package com.byzquant.fed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "macro_indicators")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MacroIndicator {

    @Id
    @Column(name = "id", length = 50)
    private String id; // WALCL, FEDFUNDS, M2SL vb.

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "category", nullable = false, length = 50)
    private String category; // PARA_POLITIKASI, ENFLASYON, SISTEMIK_RISK

    @Column(name = "fred_code", nullable = false, unique = true, length = 50)
    private String fredCode;
}
