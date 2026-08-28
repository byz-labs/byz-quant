package com.byzquant.fed.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EconomicDataResponse(
    String realtime_start,
    String realtime_end,
    String order_by,
    String sort_order,
    int count,
    List<ObservationDto> observations
) {}
