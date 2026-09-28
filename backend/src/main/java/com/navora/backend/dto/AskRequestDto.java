package com.navora.backend.dto;

import java.util.UUID;

public record AskRequestDto(UUID sessionId, String question) {
}
