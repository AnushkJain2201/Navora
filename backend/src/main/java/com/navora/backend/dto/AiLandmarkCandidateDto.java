package com.navora.backend.dto;

public record AiLandmarkCandidateDto(
        String id,
        String name,
        String category,
        String description
) {
}
