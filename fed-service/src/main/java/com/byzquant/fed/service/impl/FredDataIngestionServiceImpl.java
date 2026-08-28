package com.byzquant.fed.service.impl;

import com.byzquant.fed.client.impl.FredRestClient;
import com.byzquant.fed.dto.FredResponse;
import com.byzquant.fed.entity.MacroObservation;
import com.byzquant.fed.repository.MacroObservationRepository;
import com.byzquant.fed.service.FredDataIngestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FredDataIngestionServiceImpl implements FredDataIngestionService {

    private final FredRestClient fredRestClient;
    private final MacroObservationRepository observationRepository;

    public FredDataIngestionServiceImpl(FredRestClient fredRestClient, MacroObservationRepository observationRepository) {
        this.fredRestClient = fredRestClient;
        this.observationRepository = observationRepository;
    }

    @Override
    public MacroObservationRepository getObservationRepository() {
        return this.observationRepository;
    }

    @Override
    @Transactional // 🚀 Thread'lerin veritabanı oturumlarını izole etmek için katı kurumsal kilit!
    public void ingestIndicator(String indicatorId) {
        String cleanId = indicatorId.trim().toUpperCase();
        String startDate = LocalDate.now().minusYears(10).toString();
        
        String endpoint = "/series/observations?series_id={series_id}&observation_start={observation_start}";
        
        if ("CPILFESL".equals(cleanId) || "PCEPILFE".equals(cleanId)) {
            endpoint += "&units=pc1"; 
        }

        try {
            FredResponse response = fredRestClient.fetch(endpoint, FredResponse.class, cleanId, startDate);

            if (response != null && response.observations() != null) {
                // 🚀 RACE CONDITION ÇÖZÜMÜ 1: Bütün DB'yi çekmek yerine, 
                // SADECE bu canavara ait mevcuttaki kayıtları taze taze çekip thread güvenliğini sağlıyoruz!
                List<MacroObservation> existingOfThisIndicator = observationRepository.findAll().stream()
                        .filter(o -> o.getIndicatorId().equals(cleanId))
                        .toList();

                List<MacroObservation> entitiesToSave = new ArrayList<>();

                response.observations().forEach(obs -> {
                    if (!".".equals(obs.value())) {
                        LocalDate obsDate = LocalDate.parse(obs.date());
                        BigDecimal obsValue = new BigDecimal(obs.value());

                        // Sadece bu göstergenin kendi geçmişi içinde eşleşme arıyoruz şef
                        java.util.Optional<MacroObservation> existing = existingOfThisIndicator.stream()
                            .filter(o -> o.getObservationDate().equals(obsDate))
                            .findFirst();

                        MacroObservation observation = MacroObservation.builder()
                            .indicatorId(cleanId)
                            .observationDate(obsDate)
                            .observationValue(obsValue)
                            .build();

                        if (existing.isPresent()) {
                            // Eğer kayıt zaten varsa, ID'sini üzerine çakarak INSERT yerine UPDATE nizamına zorla!
                            observation.setId(existing.get().getId());
                        }
                        entitiesToSave.add(observation);
                    }
                });

                if (!entitiesToSave.isEmpty()) {
                    observationRepository.saveAll(entitiesToSave);
                    log.info("✓ [{}] canavarı için {} adet veri (Race-Safe Upsert) başarıyla PostgreSQL'e mühürlendi!", cleanId, entitiesToSave.size());
                }
            }
        } catch (Exception e) {
            log.error("🚨 [{}] Emme Operasyonunda Kritik Hata: {}", cleanId, e.getMessage());
        }
    }

    @Override
    public void ingestAllIndicators(List<String> indicatorIds) {
        // 14 Canavarı multi-thread olarak aslanlar gibi koşturmaya devam şef!
        indicatorIds.parallelStream().forEach(this::ingestIndicator);
    }
}
