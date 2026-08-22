package com.byzquant.fed.adapters.rest;

import com.byzquant.fed.domain.FedCategory;
import com.byzquant.fed.ports.FedDataPort;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class FedRestAdapter implements FedDataPort {

    private final WebClient webClient;
    private final String apiKey;

    public FedRestAdapter(WebClient webClient, String apiKey) {
        this.webClient = webClient;
        this.apiKey = apiKey;
    }

    @Override
    public Optional<FedCategory> fetchCategoryById(Long id) {
        return Optional.ofNullable(id)
            .flatMap(categoryId -> executeGetRequest("/category", categoryId))
            .stream()
            .flatMap(response -> Optional.ofNullable(response.categories()).stream())
            .flatMap(List::stream)
            .findFirst()
            .map(this::toDomain);
    }

    @Override
    public List<FedCategory> fetchChildrenByParentId(Long parentId) {
        return Optional.ofNullable(parentId)
            .flatMap(pId -> executeGetRequest("/category/children", pId))
            .stream()
            .flatMap(response -> Optional.ofNullable(response.categories()).stream())
            .flatMap(List::stream)
            .map(this::toDomain)
            .toList();
    }

    // 🔒 JENERİK REAKTİF HTTP MOTORU: Tüm kod tekrarını tek merkezde toplayan gövde
    private Optional<FedCategoryResponse> executeGetRequest(String path, Long categoryId) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("category_id", categoryId)
                .queryParam("api_key", apiKey)
                .queryParam("file_type", "json")
                .build())
            .retrieve()
            .bodyToMono(FedCategoryResponse.class)
            .timeout(Duration.ofSeconds(2)) // 2 saniyelik katı barajımız tek noktada
            .blockOptional();
    }

    // 🔄 REAKTOR MAPPER: DTO'dan temiz Domain nesnesine dönüşüm
    private FedCategory toDomain(FedCategoryResponse.CategoryDto dto) {
        return new FedCategory(dto.id(), dto.name(), Optional.ofNullable(dto.parentId()));
    }
}
