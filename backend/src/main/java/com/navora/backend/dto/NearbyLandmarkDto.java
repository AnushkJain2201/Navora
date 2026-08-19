package com.navora.backend.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record NearbyLandmarkDto(
        UUID id,
        String name,
        String category,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        double distanceMeters
) {
}
