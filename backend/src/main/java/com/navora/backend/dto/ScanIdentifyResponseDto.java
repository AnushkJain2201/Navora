package com.navora.backend.dto;

import java.util.List;
import java.util.UUID;

public record ScanIdentifyResponseDto(
        UUID scanId,
        String imageUrl,
        List<NearbyLandmarkDto> candidateLandmarks
) {
}
