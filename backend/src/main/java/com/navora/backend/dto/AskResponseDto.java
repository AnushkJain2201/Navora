package com.navora.backend.dto;

import java.util.List;
import java.util.UUID;

public record AskResponseDto(UUID sessionId, String answer, List<String> sources) {
}
