package com.byzquant.fed.dto;

import com.byzquant.fed.entity.FedSeriesEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FedSeriesResponse {
    
    // FED API JSON formatında anahtar kelime 'seriess' olarak döner
    private List<SeriesDto> seriess;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SeriesDto {
        private String id;
        private String title;
        private String frequency;
        private String units;

        public FedSeriesEntity toEntity(Long categoryId) {
            FedSeriesEntity entity = new FedSeriesEntity();
            entity.setId(this.id);
            entity.setTitle(this.title);
            entity.setFrequency(this.frequency);
            entity.setUnits(this.units);
            entity.setCategoryId(categoryId);
            return entity;
        }
    }
}
