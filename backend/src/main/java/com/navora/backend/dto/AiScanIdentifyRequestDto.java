package com.navora.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AiScanIdentifyRequestDto(
        @JsonProperty("image_base64") String imageBase64,
        List<AiLandmarkCandidateDto> candidates
) {
}
