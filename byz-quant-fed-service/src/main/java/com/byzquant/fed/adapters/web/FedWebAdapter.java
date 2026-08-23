package com.byzquant.fed.adapters.web;

import com.byzquant.fed.domain.FedCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fed/categories")
public class FedWebAdapter {

    private final FedCategoryService fedCategoryService;

    public FedWebAdapter(FedCategoryService fedCategoryService) {
        this.fedCategoryService = fedCategoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FedCategoryWebResponse> getCategoryById(@PathVariable Long id) {
        return fedCategoryService.getCategory(id)
                .map(category -> new FedCategoryWebResponse(category.id(), category.name()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Alt ağaç hiyerarşisini liste halinde Postman'e uçuran fonksiyonel endpoint
    @GetMapping("/{id}/children")
    public ResponseEntity<List<FedCategoryWebResponse>> getChildrenByParentId(@PathVariable Long id) {
        List<FedCategoryWebResponse> responses = fedCategoryService.getChildrenCategories(id).stream()
                .map(category -> new FedCategoryWebResponse(category.id(), category.name()))
                .toList();

        return ResponseEntity.ok(responses);
    }

    //Yatay korelasyon listesini dış dünyaya sunan fonksiyonel endpoint
    @GetMapping("/{id}/related")
    public ResponseEntity<List<FedCategoryWebResponse>> getRelatedByCategoryId(@PathVariable Long id) {
        List<FedCategoryWebResponse> responses = fedCategoryService.getRelatedCategories(id).stream()
                .map(category -> new FedCategoryWebResponse(category.id(), category.name()))
                .toList();

        return ResponseEntity.ok(responses);
    }

    private record FedCategoryWebResponse(Long id, String name) {
    }
}
