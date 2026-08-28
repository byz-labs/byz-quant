package com.byzquant.fed.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ObservationDto(
    String realtime_start,
    String realtime_end,
    String date,
    String value
) {}
