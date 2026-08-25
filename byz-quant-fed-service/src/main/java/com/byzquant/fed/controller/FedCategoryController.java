package com.byzquant.fed.controller;

import com.byzquant.fed.dto.FedAnalysisResult;
import com.byzquant.fed.dto.FedCompositeReport;
import com.byzquant.fed.service.FedAnalysisService;
import com.byzquant.fed.service.FedCategoryService;
import com.byzquant.fed.service.FedCompositeAnalysisService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fed/categories")
@RequiredArgsConstructor
public class FedCategoryController {

    private final FedCategoryService fedCategoryService;
    private final FedAnalysisService fedAnalysisService;
    private final FedCompositeAnalysisService fedCompositeAnalysisService;

    // YENİ: Tekil kategori bilgisini dönen ana endpoint
    // (localhost:8080/api/v1/fed/categories/0)
    @GetMapping("/{id}")
    public ResponseEntity<FedCategoryWebResponse> getCategoryById(@PathVariable Long id) {
        return fedCategoryService.getCategory(id)
                .map(entity -> ResponseEntity.ok(new FedCategoryWebResponse(entity.getId(), entity.getName())))
                .orElse(ResponseEntity.notFound().build());
    }

    // Çocukları (Dikey) listele ve PostgreSQL'e akıt
    @GetMapping("/{id}/children")
    public ResponseEntity<List<FedCategoryWebResponse>> getChildren(@PathVariable Long id) {
        List<FedCategoryWebResponse> responses = fedCategoryService.getChildrenCategories(id).stream()
                .map(entity -> new FedCategoryWebResponse(entity.getId(), entity.getName()))
                .toList();
        return ResponseEntity.ok(responses);
    }

    // İlişkilileri (Yatay) listele ve Ara Tabloya akıt
    @GetMapping("/{id}/related")
    public ResponseEntity<List<FedCategoryWebResponse>> getRelated(@PathVariable Long id) {
        List<FedCategoryWebResponse> responses = fedCategoryService.getRelatedCategories(id).stream()
                .map(entity -> new FedCategoryWebResponse(entity.getId(), entity.getName()))
                .toList();
        return ResponseEntity.ok(responses);
    }

    private record FedCategoryWebResponse(Long id, String name) {
    }

    // YENİ: Kategorinin altındaki tüm ekonomik zaman serilerini listeler (Lokal
    // DB'ye de yazar)
    @GetMapping("/{id}/series")
    public ResponseEntity<List<FedSeriesWebResponse>> getSeriesByCategoryId(@PathVariable Long id) {
        List<FedSeriesWebResponse> responses = fedCategoryService.getSeriesByCategoryId(id).stream()
                .map(entity -> new FedSeriesWebResponse(entity.getId(), entity.getTitle(), entity.getFrequency(),
                        entity.getUnits()))
                .toList();
        return ResponseEntity.ok(responses);
    }

    private record FedSeriesWebResponse(String id, String title, String frequency, String units) {
    }

    // YENİ: Belirli bir ekonomik serinin tüm tarihsel fiyat/değer satırlarını döner
    // (Lokal DB'ye de yazar)
    @GetMapping("/series/{seriesId}/observations")
    public ResponseEntity<List<FedObservationWebResponse>> getObservations(@PathVariable String seriesId) {
        List<FedObservationWebResponse> responses = fedCategoryService.getSeriesObservations(seriesId).stream()
                .map(entity -> new FedObservationWebResponse(entity.getDate().toString(), entity.getValue()))
                .toList();
        return ResponseEntity.ok(responses);
    }

    private record FedObservationWebResponse(String date, String value) {
    }

    // KESİN ÇÖZÜM: 7 efsanevi serinin tamamını tek bir endpoint adresi üzerinden
    // analiz ettiriyoruz!
    @GetMapping("/analysis/{seriesId}")
    public ResponseEntity<FedAnalysisResult> getEconomicIndicatorAnalysis(@PathVariable String seriesId) {
        return ResponseEntity.ok(fedAnalysisService.analyzeIndicator(seriesId));
    }

    // YENİ: Küresel piyasaların mutlak 'Risk-On / Risk-Off' şalterini yöneten
    // Kompozit Rapor Endpoint'i
    // KESİN ÇÖZÜM: Builder'dan gelen tüm alanları (Hazine Milyar doları dahil)
    // eksiksiz ve null olmadan JSON'a basan temiz kapı
    // KESİN ÇÖZÜM: Başına ve sonuna hiçbir ara record koymadan, doğrudan asıl DTO
    // nesnesini Jackson'a teslim ediyoruz!
    @GetMapping("/analysis/composite-intelligence")
    public ResponseEntity<FedCompositeReport> getCompositeIntelligenceReport() {
        FedCompositeReport report = fedCompositeAnalysisService.generateCompositeReport();
        return ResponseEntity.ok(report);
    }
}
