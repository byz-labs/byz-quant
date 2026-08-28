package com.byzquant.fed.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// 🚀 SİHİRLİ SATIR: FRED'den gelen gereksiz tüm fazla alanları çöpe at, patlama!
@JsonIgnoreProperties(ignoreUnknown = true)
public record FredResponse(
    @JsonProperty("realtime_start") String realtimeStart,
    @JsonProperty("realtime_end") String realtimeEnd,
    List<FredObservationDTO> observations
) {}
