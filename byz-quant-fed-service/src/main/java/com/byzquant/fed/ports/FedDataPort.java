package com.byzquant.fed.ports;

import com.byzquant.fed.domain.FedCategory;
import java.util.List;
import java.util.Optional;

public interface FedDataPort {
    Optional<FedCategory> fetchCategoryById(Long id);
    
    // YENİ: Belirli bir üst kategoriye ait tüm alt çocukları liste olarak getirir
    List<FedCategory> fetchChildrenByParentId(Long parentId);
}
