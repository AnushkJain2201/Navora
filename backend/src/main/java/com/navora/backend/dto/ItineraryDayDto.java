package com.navora.backend.dto;

import java.util.List;
import java.util.UUID;

public record ItineraryDayDto(
        UUID id,
        Integer dayNumber,
        List<ItineraryStopDto> stops
) {}
