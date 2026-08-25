package com.byzquant.fed.dto;

import com.byzquant.fed.entity.FedCategoryEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FedCategoryResponse {
    
    private List<CategoryDto> categories;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CategoryDto {
        private Long id;
        private String name;
        
        @JsonProperty("parent_id") // Sinsi snake_case hatasını kökten çözen satır
        private Long parentId;

        // 🔄 INNER MAPPER: Servis katmanını tertemiz yapan, doğrudan Entity üreten akıllı metot
        public FedCategoryEntity toEntity() {
            FedCategoryEntity entity = new FedCategoryEntity();
            entity.setId(this.id);
            entity.setName(this.name);
            entity.setParentId(this.parentId); // Entity içindeki akıllı döngü kırıcı (setParentId) devreye girer!
            return entity;
        }
    }
}
