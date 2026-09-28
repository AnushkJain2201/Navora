package com.navora.backend.dto;

import java.util.List;

public record AiAskResponseDto(String answer, List<String> sources) {
}
