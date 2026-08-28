package com.byzquant.fed.service;

import com.byzquant.fed.repository.MacroObservationRepository;
import java.util.List;

public interface FredDataIngestionService {
    
    // Controller katmanının polimorfik olarak diske erişmesini sağlayan asil getter
    MacroObservationRepository getObservationRepository();

    // Tek bir göstergeyi (Örn: WALCL) FRED'den emen metot
    void ingestIndicator(String indicatorId);

    // 14 Canavarın tamamını aynı anda multi-thread emen o paralel motor şef!
    void ingestAllIndicators(List<String> indicatorIds);
}
