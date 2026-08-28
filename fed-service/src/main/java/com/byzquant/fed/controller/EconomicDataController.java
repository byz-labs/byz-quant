package com.byzquant.fed.controller;

import com.byzquant.fed.dto.EconomicDataResponse;
import com.byzquant.fed.service.EconomicDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/economic")
@CrossOrigin(origins = "http://localhost:3000")
public class EconomicDataController {

    private final EconomicDataService economicDataService;

    public EconomicDataController(EconomicDataService economicDataService) {
        this.economicDataService = economicDataService;
    }

    @GetMapping("/series/{seriesId}")
    public ResponseEntity<EconomicDataResponse> getFedData(@PathVariable String seriesId) {
        return ResponseEntity.ok(economicDataService.getIndicatorData(seriesId));
    }

    @GetMapping("/calendar")
    public ResponseEntity<com.byzquant.fed.dto.ReleaseDatesResponse> getCalendar() {
        // Servis katmanındaki takvim metodunu dışarı açıyoruz
        return ResponseEntity.ok(economicDataService.getReleaseCalendar());
    }
}
