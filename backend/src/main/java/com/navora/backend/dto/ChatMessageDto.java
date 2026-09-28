package com.navora.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageDto(UUID id, String role, String content, LocalDateTime createdAt) {
}
