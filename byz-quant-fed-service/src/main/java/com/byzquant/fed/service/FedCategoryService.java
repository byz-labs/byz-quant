package com.byzquant.fed.service;

import com.byzquant.fed.entity.FedCategoryEntity;
import com.byzquant.fed.entity.FedCategoryRelationEntity;
import com.byzquant.fed.entity.FedObservationEntity;
import com.byzquant.fed.entity.FedReleaseCalendarEntity;
import com.byzquant.fed.entity.FedSeriesEntity;
import com.byzquant.fed.repository.FedCategoryRepository;
import com.byzquant.fed.repository.FedObservationRepository;
import com.byzquant.fed.repository.FedReleaseCalendarRepository;
import com.byzquant.fed.repository.FedSeriesRepository;
import com.byzquant.fed.repository.FedCategoryRelationRepository;
import com.byzquant.fed.dto.FedCategoryResponse;
import com.byzquant.fed.dto.FedObservationResponse;
import com.byzquant.fed.dto.FedReleaseDatesResponse;
import com.byzquant.fed.dto.FedSeriesResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.RequiredArgsConstructor;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Sadece final alanları enjekte eden saf Lombok konforu
public class FedCategoryService {

    private final WebClient fedWebClient; // Merkezî bean otomatik olarak enjekte ediliyor!
    private final FedCategoryRepository categoryRepository;
    private final FedCategoryRelationRepository relationRepository;
    private final FedSeriesRepository seriesRepository; // YENİ: Seriler için işçi enjeksiyonu
    private final FedObservationRepository observationRepository; // YENİ: Ham veriler için işçi enjeksiyonu
    private final FedReleaseCalendarRepository calendarRepository; // En üste ekleyin

    @Value("${fed.api.key}")
    private String apiKey;

    // YENİ: Tekil kategori detayını çeker ve PostgreSQL ana tablosunda günceller
    public Optional<FedCategoryEntity> getCategory(Long id) {
        return Optional.ofNullable(id)
                .flatMap(cId -> executeGetRequest("/category", cId))
                .stream()
                .flatMap(response -> Optional.ofNullable(response.getCategories()).stream())
                .flatMap(List::stream)
                .findFirst() // Tekil kategori detayı geleceği için listeden ilk elemanı seçiyoruz
                .map(FedCategoryResponse.CategoryDto::toEntity)
                .map(categoryRepository::save); // DB'ye kaydet/güncelle
    }

    // Dikey Çocuk Kategoriler
    public List<FedCategoryEntity> getChildrenCategories(Long parentId) {
        return Optional.ofNullable(parentId)
                .flatMap(pId -> executeGetRequest("/category/children", pId))
                .stream()
                .flatMap(response -> Optional.ofNullable(response.getCategories()).stream())
                .flatMap(List::stream)
                .map(FedCategoryResponse.CategoryDto::toEntity)
                .map(categoryRepository::save)
                .toList();
    }

    // Yatay İlişkili Kategoriler
    public List<FedCategoryEntity> getRelatedCategories(Long categoryId) {
        return Optional.ofNullable(categoryId)
                .flatMap(cId -> executeGetRequest("/category/related", cId))
                .stream()
                .flatMap(response -> Optional.ofNullable(response.getCategories()).stream())
                .flatMap(List::stream)
                .map(dto -> {
                    FedCategoryEntity entity = dto.toEntity();
                    categoryRepository.save(entity);

                    relationRepository.save(new FedCategoryRelationEntity(categoryId, entity.getId()));
                    return entity;
                })
                .toList();
    }

    private Optional<FedCategoryResponse> executeGetRequest(String path, Long categoryId) {
        return fedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("category_id", categoryId)
                        .queryParam("api_key", apiKey)
                        .queryParam("file_type", "json")
                        .build())
                .retrieve()
                .bodyToMono(FedCategoryResponse.class)
                .timeout(Duration.ofSeconds(2))
                .blockOptional();
    }

    // YENİ: Kategori altındaki serileri internetten çeker ve PostgreSQL'e kaydeder
    public List<FedSeriesEntity> getSeriesByCategoryId(Long categoryId) {
        return Optional.ofNullable(categoryId)
                .flatMap(cId -> executeGetSeriesRequest("/category/series", cId))
                .stream()
                .flatMap(response -> Optional.ofNullable(response.getSeriess()).stream())
                .flatMap(List::stream)
                .map(dto -> dto.toEntity(categoryId)) // Doğrudan kategori bağıyla Entity'ye dönüşüm
                .map(seriesRepository::save) // PostgreSQL'e mühürle
                .toList();
    }

    // Seriler için özel Jenerik HTTP Motoru çağrısı
    private Optional<FedSeriesResponse> executeGetSeriesRequest(String path, Long categoryId) {
        return fedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("category_id", categoryId)
                        .queryParam("api_key", apiKey)
                        .queryParam("file_type", "json")
                        .build())
                .retrieve()
                .bodyToMono(FedSeriesResponse.class)
                .timeout(Duration.ofSeconds(5)) // Seriler listesi kalabalık olabileceği için zaman aşımını 5sn yaptık
                .blockOptional();
    }

    // YENİ: Serinin tüm tarihsel verilerini internetten çeker ve PostgreSQL'e
    // kaydeder
    public List<FedObservationEntity> getSeriesObservations(String seriesId) {
        return Optional.ofNullable(seriesId)
                .flatMap(sId -> executeGetObservationsRequest("/series/observations", sId))
                .stream()
                .flatMap(response -> Optional.ofNullable(response.getObservations()).stream())
                .flatMap(List::stream)
                .map(dto -> dto.toEntity(seriesId))
                .filter(Objects::nonNull) // Boş gelen ('.') verileri eledik, veri ambarımız tertemiz kalacak!
                .map(observationRepository::save) // PostgreSQL'e mühürle
                .toList();
    }

    // Observations için özel Jenerik HTTP Motoru çağrısı
    private Optional<FedObservationResponse> executeGetObservationsRequest(String path, String seriesId) {
        return fedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("series_id", seriesId)
                        .queryParam("api_key", apiKey)
                        .queryParam("file_type", "json")
                        .build())
                .retrieve()
                .bodyToMono(FedObservationResponse.class)
                .timeout(Duration.ofSeconds(10)) // Veri boyutu 50 yıllık olabileceği için zaman aşımını 10sn yaptık
                .blockOptional();
    }

    // 1. TAKVİM TAZELEME MOTORU: FED API'den gelecek yayın takvim tarihlerini yerel
    // DB'ye işler
    public void syncReleaseCalendar(String releaseId, String seriesId) {
        fedWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/release/dates")
                        .queryParam("release_id", releaseId)
                        .queryParam("api_key", apiKey)
                        .queryParam("file_type", "json")
                        .build())
                .retrieve()
                .bodyToMono(FedReleaseDatesResponse.class)
                .map(response -> Optional.ofNullable(response.getRelease_dates()).orElse(List.of()))
                .flatMapIterable(list -> list)
                .map(dto -> FedReleaseCalendarEntity.builder()
                        .seriesId(seriesId)
                        .releaseDate(LocalDate.parse(dto.getDate()))
                        .releaseTime(LocalTime.of(8, 30)) // FED makro verileri standart Doğu Saati ile 08:30'da açıklar
                        .isProcessed(false)
                        .build())
                .map(calendarRepository::save)
                .subscribe(); // Arka planda reaktif asenkron yazsın
    }

    // 2. RETRY-CHECK İŞÇİSİ: Bugün yayınlanması beklenen seriyi internetten
    // tırmalar
    public void checkAndFetchScheduledData() {
        LocalDate today = LocalDate.now();
        List<FedReleaseCalendarEntity> todaysTasks = calendarRepository.findByReleaseDateAndIsProcessedFalse(today);

        for (FedReleaseCalendarEntity task : todaysTasks) {
            // Veriyi çekmeyi dene (Daha önce yazdığımız o muazzam metod)
            List<FedObservationEntity> newObservations = getSeriesObservations(task.getSeriesId());

            // Eğer internetten gelen en son verinin tarihi bugüne eşitse, veri resmen
            // YAYINLANMIŞ demektir!
            boolean isDataArrived = newObservations.stream()
                    .anyMatch(o -> o.getDate().equals(today) || o.getDate().isAfter(today.minusDays(5))); // Aylık/Çeyreklik
                                                                                                          // gecikme
                                                                                                          // kontrolü

            if (isDataArrived) {
                task.setIsProcessed(true); // Görevi yeşile boya, bir daha boşuna tırmalamasın
                calendarRepository.save(task);
                System.out.println("✅ BYZ QUANT DATA PIPELINE SUCCESS: " + task.getSeriesId()
                        + " verisi başarıyla yakalandı ve DB mühürlendi!");
            } else {
                System.out.println(
                        "⚠️ WATCHDOG ALERT: " + task.getSeriesId() + " için henüz veri düşmedi. Yeniden denenecek...");
            }
        }
    }
}
