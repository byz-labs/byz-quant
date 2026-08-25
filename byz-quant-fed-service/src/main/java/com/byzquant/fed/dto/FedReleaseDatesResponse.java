package com.byzquant.fed.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FedReleaseDatesResponse {
    
    private List<ReleaseDateDto> release_dates;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReleaseDateDto {
        private String release_id;
        private String date; // YYYY-MM-DD
    }
}
