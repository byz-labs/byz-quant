package com.byzquant.fed.dto;

import com.byzquant.fed.entity.FedObservationEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FedObservationResponse {
    
    private List<ObservationDto> observations;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ObservationDto {
        private String date; // JSON'dan string (YYYY-MM-DD) olarak gelir
        private String value; // Rakam veya '.' gelebilir

        public FedObservationEntity toEntity(String seriesId) {
            // FED'in resmi tatillerde veya veri eksikliğinde bastığı '.' (boş) satırları filtrelemek için business kontrolü
            if (this.value == null || this.value.trim().equals(".")) {
                return null;
            }
            FedObservationEntity entity = new FedObservationEntity();
            entity.setSeriesId(seriesId);
            entity.setDate(LocalDate.parse(this.date));
            entity.setValue(this.value.trim());
            return entity;
        }
    }
}
