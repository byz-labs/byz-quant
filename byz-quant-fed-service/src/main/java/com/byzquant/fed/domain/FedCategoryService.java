package com.byzquant.fed.domain;

import com.byzquant.fed.ports.FedDataPort;
import com.byzquant.fed.ports.FedPersistencePort;
import java.util.List;
import java.util.Optional;

public class FedCategoryService {

    private final FedDataPort fedDataPort; // İnternet (REST) okuma kapısı
    private final FedPersistencePort fedPersistencePort; // Veritabanı (DB) yazma kapısı

    // SOLID - Dependency Inversion: Servis somut sınıfları bilmez, sadece iki soyut
    // portu tanır
    public FedCategoryService(FedDataPort fedDataPort, FedPersistencePort fedPersistencePort) {
        this.fedDataPort = fedDataPort;
        this.fedPersistencePort = fedPersistencePort;
    }

    public Optional<FedCategory> getCategory(Long id) {
        return Optional.ofNullable(id)
                .flatMap(fedDataPort::fetchCategoryById) // Her zaman internete git (En taze veri)
                .map(category -> {
                    fedPersistencePort.save(category); // DB'dekini en taze veriyle güncelle/kaydet
                    return category;
                });
    }

    public List<FedCategory> getChildrenCategories(Long parentId) {
        return Optional.ofNullable(parentId)
                .map(fedDataPort::fetchChildrenByParentId) // 1. İnternetten listeyi çek
                .map(list -> {
                    fedPersistencePort.saveAll(list); // 2. Çekilen listeyi tek hamlede DB'ye göm (Side-Effect)
                    return list; // 3. Akışın devam etmesi için listeyi aynen pasla
                })
                .orElse(List.of()); // 4. Null veya hata durumunda güvenli boş liste dön
    }

}
