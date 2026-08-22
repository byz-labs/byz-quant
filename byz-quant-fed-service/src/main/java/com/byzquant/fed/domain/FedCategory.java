package com.byzquant.fed.domain;

import java.util.Optional;

public record FedCategory(
    Long id,
    String name,
    Optional<Long> parentId
) {
    // Fonksiyonel kompakt constructor (if bloklarından kaçınmak için erken validasyon)
    public FedCategory {
        Optional.ofNullable(id)
            .filter(currentId -> name != null && !name.isBlank())
            .orElseThrow(() -> new IllegalArgumentException("Category ID and Name cannot be null or empty"));
    }
}
