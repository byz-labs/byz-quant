package com.byzquant.fed.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FredObservationDTO(
    String date,
    String value
) {}
