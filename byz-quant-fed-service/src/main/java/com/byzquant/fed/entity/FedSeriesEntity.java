package com.byzquant.fed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fed_series")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FedSeriesEntity {

    @Id
    @Column(name = "id")
    private String id; // FED serileri string anahtarlardır (Örn: 'GDPC1', 'UNRATE')

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "frequency")
    private String frequency; // Quarterly, Monthly vb.

    @Column(name = "units")
    private String units; // Billions of Chained 2017 Dollars vb.

    @Column(name = "category_id", nullable = false)
    private Long categoryId; // Serinin bağlı olduğu üst kategori klasörü
}
