package com.byzquant.fed.adapters.persistence;

import com.byzquant.fed.domain.FedCategory;
import com.byzquant.fed.ports.FedDataPort;
import com.byzquant.fed.ports.FedPersistencePort;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class FedPersistenceAdapter implements FedDataPort, FedPersistencePort {

    private final FedCategoryRepository repository;

    public FedPersistenceAdapter(FedCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(FedCategory category) {
        Optional.ofNullable(category)
            .map(domain -> new FedCategoryEntity(
                domain.id(),
                domain.name(),
                // KESİN ÇÖZÜM: Optional paketini düzgünce patlatıp ham Long değerini alıyoruz
                domain.parentId().orElse(null) 
            ))
            .ifPresent(repository::save);
    }

    @Override
    public void saveAll(List<FedCategory> categories) {
        Optional.ofNullable(categories)
            .filter(list -> !list.isEmpty())
            .map(list -> list.stream()
                .map(domain -> new FedCategoryEntity(
                    domain.id(),
                    domain.name(),
                    // KESİN ÇÖZÜM: Toplu kayıtta da Optional sarmalından ham Long veriyi çıkarıyoruz
                    domain.parentId().orElse(null) 
                ))
                .toList())
            .ifPresent(repository::saveAll);
    }

    @Override
    public Optional<FedCategory> fetchCategoryById(Long id) {
        return repository.findById(id)
            .map(entity -> new FedCategory(
                entity.getId(),
                entity.getName(),
                Optional.ofNullable(entity.getParentId())
            ));
    }

    @Override
    public List<FedCategory> fetchChildrenByParentId(Long parentId) {
        return List.of();
    }
}
