package com.navora.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AiItineraryStopDto(
        String name,
        String description,
        @JsonProperty("estimated_duration_hours") Double estimatedDurationHours
) {}
