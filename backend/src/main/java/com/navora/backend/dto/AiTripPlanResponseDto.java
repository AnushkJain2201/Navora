package com.navora.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AiTripPlanResponseDto(
        String destination,
        @JsonProperty("duration_days") Integer durationDays,
        Double budget,
        List<AiItineraryDayDto> itinerary,
        @JsonProperty("clarification_message") String clarificationMessage
) {}
