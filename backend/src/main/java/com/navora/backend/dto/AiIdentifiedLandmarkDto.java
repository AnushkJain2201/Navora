package com.navora.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AiIdentifiedLandmarkDto(
        boolean matched,
        @JsonProperty("landmark_id") String landmarkId,
        @JsonProperty("landmark_name") String landmarkName,
        @JsonProperty("specific_feature") String specificFeature,
        @JsonProperty("confidence_reason") String confidenceReason,
        @JsonProperty("generated_context") String generatedContext
) {
}
