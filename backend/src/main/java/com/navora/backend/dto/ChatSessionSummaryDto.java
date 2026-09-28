package com.navora.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatSessionSummaryDto(UUID id, String title, LocalDateTime createdAt) {
}
