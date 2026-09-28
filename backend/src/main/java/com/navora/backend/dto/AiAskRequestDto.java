package com.navora.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AiAskRequestDto(String question,
                              @JsonProperty("conversation_history")List<AiChatMessageDto> conversationHistory) {
}
