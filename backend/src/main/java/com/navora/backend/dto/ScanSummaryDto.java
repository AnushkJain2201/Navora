package com.navora.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScanSummaryDto(
        UUID id,
        String imageUrl,
        boolean matched,
        String landmarkName,
        String specificFeature,
        String generatedContext,
        LocalDateTime scannedAt
) {
}
