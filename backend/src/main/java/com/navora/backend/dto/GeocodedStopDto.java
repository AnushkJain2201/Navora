package com.navora.backend.dto;

public record GeocodedStopDto(
        int originalIndex,
        String landmarkName,
        GeoCoordinateDto coordinate
) {
}
