package com.byzquant.fed.adapters.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FedCategoryResponse(
    List<CategoryDto> categories
) {
    public record CategoryDto(
        Long id, 
        String name, 
        
        // KESİN ÇÖZÜM: Record yapısında sinsi null eşleşme hatasını kökten bitiren net tanım
        @JsonProperty("parent_id") 
        Long parentId 
    ) {}
}
