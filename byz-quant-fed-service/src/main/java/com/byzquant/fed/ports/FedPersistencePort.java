package com.byzquant.fed.ports;

import java.util.List;

import com.byzquant.fed.domain.FedCategory;

public interface FedPersistencePort {
    // Veritabanına domain modelini kaydetme emri
    void save(FedCategory category);

    // Toplu kayıt (Batch Insert)
    void saveAll(List<FedCategory> categories);

    // İki kategori arasındaki yatay ilişkiyi DB'ye kaydeder
    void saveRelation(Long categoryId, Long relatedCategoryId);
}
