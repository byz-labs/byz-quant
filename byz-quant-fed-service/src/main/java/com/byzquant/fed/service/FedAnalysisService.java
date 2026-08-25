package com.byzquant.fed.service;

import com.byzquant.fed.entity.FedObservationEntity;
import com.byzquant.fed.repository.FedObservationRepository;
import com.byzquant.fed.dto.FedAnalysisResult;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FedAnalysisService {

    private final FedObservationRepository observationRepository;

    // KESİN ÇÖZÜM: 7 serinin (veya 1000 serinin) tamamını tek merkezden analiz eden dinamik motor
    public FedAnalysisResult analyzeIndicator(String seriesId) {
        String upperSeriesId = seriesId.toUpperCase();
        
        // 1. İstenen serinin ham verilerini kronolojik sırada DB'den çek
        List<FedObservationEntity> observations = observationRepository.findBySeriesIdOrderByDateAsc(upperSeriesId);

        if (observations.isEmpty()) {
            throw new IllegalArgumentException(String.format("Veritabanında '%s' serisine ait ham veri bulunamadı!", upperSeriesId));
        }

        // 2. En güncel (Mevcut) veri satırını yakala
        FedObservationEntity latest = observations.get(observations.size() - 1);
        Double currentRate = Double.parseDouble(latest.getValue());
        
        // 3. Geçmiş (1 Yıl Önceki) veri satırını yakala (Aylık/Çeyreklik/Günlük frekansa göre güvenli indeks)
        int oneYearAgoIndex = Math.max(0, observations.size() - 12); 
        FedObservationEntity oneYearAgo = observations.get(oneYearAgoIndex);
        Double pastRate = Double.parseDouble(oneYearAgo.getValue());

        // 4. Momentum ve Trend Yönü Hesaplama
        Double yearlyChange = currentRate - pastRate;
        String trend = "FLAT (Yatay)";
        if (yearlyChange > 0.01) trend = "UPWARD (Yükseliş/Sıkılaşma)";
        else if (yearlyChange < -0.01) trend = "DOWNWARD (Düşüş/Gevşeme)";

        // Faiz oranları (FEDFUNDS, DGS10 vb.) için değişimi Baz Puan (Bps) olarak göster, diğerleri için yüzde
        boolean isInterestRate = upperSeriesId.contains("RATE") || upperSeriesId.contains("FUNDS") || upperSeriesId.contains("DGS");
        Double displayChange = isInterestRate ? yearlyChange * 100 : yearlyChange;
        String unitSign = isInterestRate ? "bps" : "%";

        // 5. Tarihsel Ekstrem Noktaları Stream ile Hesapla
        Double maxRate = observations.stream().mapToDouble(o -> Double.parseDouble(o.getValue())).max().orElse(currentRate);
        Double minRate = observations.stream().mapToDouble(o -> Double.parseDouble(o.getValue())).min().orElse(currentRate);

        // 6. Dinamik İsimlendirme ve Yorum Yapılandırması
        String summary = String.format("%s verisi şu an %s trendinde. Yıllık değişim: %.2f %s.",
                upperSeriesId, trend, displayChange, unitSign);

        return FedAnalysisResult.builder()
                .seriesId(upperSeriesId)
                .currentRate(currentRate)
                .lastUpdatedDate(latest.getDate())
                .yearlyChange(displayChange)
                .trendDirection(trend)
                .historicalMax(maxRate)
                .historicalMin(minRate)
                .analysisSummary(summary)
                .build();
    }
}
