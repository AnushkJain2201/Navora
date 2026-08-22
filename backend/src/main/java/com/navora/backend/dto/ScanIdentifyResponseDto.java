package com.navora.backend.dto;

import java.util.UUID;

public record ScanIdentifyResponseDto(
        UUID scanId,
        String imageUrl,
        boolean matched,
        String landmarkName,
        String specificFeature,
        String generatedContext
) {
}
