package com.byzquant.fed.controller;

import com.byzquant.fed.entity.MacroObservation;
import com.byzquant.fed.service.FredDataIngestionService;
import com.byzquant.fed.strategy.IndicatorStrategyFactory;
import com.byzquant.fed.strategy.IndicatorFormatStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/macro")
@CrossOrigin(origins = "*")
public class MacroDataController {

    private final FredDataIngestionService ingestionService;
    private final IndicatorStrategyFactory strategyFactory;

    public MacroDataController(FredDataIngestionService ingestionService, IndicatorStrategyFactory strategyFactory) {
        this.ingestionService = ingestionService;
        this.strategyFactory = strategyFactory;
    }

    /**
     * 🚀 SIFIR SWITCH-CASE, SIFIR IF-ELSE KONTROLÜ! SAF POLİMORFİZM!
     */
    @GetMapping("/indicator/{indicatorId}")
    public ResponseEntity<Map<String, Object>> getIndicatorData(@PathVariable String indicatorId) {
        String cleanId = indicatorId.trim().toUpperCase();

        List<MacroObservation> data = ingestionService.getObservationRepository()
                .findTop2ByIndicatorIdOrderByObservationDateDesc(cleanId);

        Map<String, Object> result = new HashMap<>();

        if (data != null && !data.isEmpty()) {
            MacroObservation latest = data.get(0);
            double rawVal = latest.getObservationValue().doubleValue();

            // 🚀 Doğrudan fabrikayı kırbaçlayıp işi nesneye yırtıyoruz şef!
            IndicatorFormatStrategy strategy = strategyFactory.getStrategy(cleanId);

            List<MacroObservation> history = ingestionService.getObservationRepository()
                    .findByIndicatorIdOrderByObservationDateDesc(cleanId);

            String formattedValue = strategy.formatValue(rawVal, history);
            
            // Eğer enflasyon hesaplandıysa grafik ölçeği de stratejiden taze döner
            double finalRawVal = strategy.scaleChartValue(rawVal);

            result.put("code", cleanId);
            result.put("value", formattedValue);
            result.put("date", latest.getObservationDate().toString());
            result.put("raw", finalRawVal);
        } else {
            result.put("code", cleanId);
            result.put("value", "...");
            result.put("date", "-");
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/indicator/{indicatorId}/series")
    public ResponseEntity<List<Map<String, Object>>> getIndicatorSeries(@PathVariable String indicatorId) {
        String cleanId = indicatorId.trim().toUpperCase();

        List<MacroObservation> indicatorData = ingestionService.getObservationRepository()
                .findByIndicatorIdOrderByObservationDateDesc(cleanId);

        IndicatorFormatStrategy strategy = strategyFactory.getStrategy(cleanId);
        int limit = strategy.getChartLimit();

        List<Map<String, Object>> chartSeries = indicatorData.stream()
                .sorted((o1, o2) -> o1.getObservationDate().compareTo(o2.getObservationDate()))
                .skip(Math.max(0, indicatorData.size() - limit))
                .map(o -> {
                    Map<String, Object> point = new HashMap<>();
                    point.put("date", o.getObservationDate().toString());
                    double scaledVal = strategy.scaleChartValue(o.getObservationValue().doubleValue());
                    point.put("value", scaledVal);
                    return point;
                })
                .toList();

        return ResponseEntity.ok(chartSeries);
    }

    @PostMapping("/admin/ingest/all")
    public ResponseEntity<String> triggerAllIngest() {
        List<String> all14Monsters = List.of(
                "WALCL", "M2SL", "FEDFUNDS", "RRPONTSYD", "DTWEXB",
                "CPILFESL", "PCEPILFE", "ICSA", "PAYEMS",
                "WLCFLPCL", "NFCI", "T10Y2Y", "MORTGAGE30US", "A191RL1Q225SBEA"
        );
        try {
            ingestionService.ingestAllIndicators(all14Monsters);
            return ResponseEntity.ok("✓ 14 Canavar mühürlendi şef!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Hata: " + e.getMessage());
        }
    }
}
