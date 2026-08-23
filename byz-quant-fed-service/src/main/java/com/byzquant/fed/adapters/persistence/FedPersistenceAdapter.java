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
    private final FedCategoryRelationRepository relationRepository; // Yeni Repository Enjeksiyonu

    public FedPersistenceAdapter(FedCategoryRepository repository, FedCategoryRelationRepository relationRepository) {
        this.repository = repository;
        this.relationRepository = relationRepository;
    }

    @Override
    public void save(FedCategory category) {
        Optional.ofNullable(category)
            .map(domain -> new FedCategoryEntity(
                domain.id(),
                domain.name(),
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
                    domain.parentId().orElse(null)
                ))
                .toList())
            .ifPresent(repository::saveAll);
    }

    //İki kategori arasındaki yatay ilişkiyi ara tabloya yazar
    @Override
    public void saveRelation(Long categoryId, Long relatedCategoryId) {
        if (categoryId != null && relatedCategoryId != null) {
            relationRepository.save(new FedCategoryRelationEntity(categoryId, relatedCategoryId));
        }
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

    // YENİ: Arayüz sözleşmesini tamamlamak için eklenen boş gövde
    @Override
    public List<FedCategory> fetchRelatedCategories(Long categoryId) {
        return List.of();
    }
}
