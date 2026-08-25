package com.byzquant.fed.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class FedAnalysisResult {
    private String seriesId;
    private String seriesName;        // Dinamik gösterge adı
    private Double currentRate;
    private LocalDate lastUpdatedDate;
    private Double yearlyChange;      // Yıllık ham değişim (Faiz için bps, işsizlik için yüzde vb.)
    private String trendDirection;    // UPWARD (Yükseliş), DOWNWARD (Düşüş), FLAT (Yatay)
    private Double historicalMax;
    private Double historicalMin;
    private String analysisSummary;   // Bot için ham metin yorumu
}
