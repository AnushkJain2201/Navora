package com.navora.backend.service;

import com.navora.backend.dto.AiAskRequestDto;
import com.navora.backend.dto.AiAskResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RagAiServiceClient {
    private final WebClient webClient;

    public RagAiServiceClient(WebClient.Builder webClientBuilder, @Value("${ai.service.url}") String aiServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(aiServiceUrl).build();
    }

    public AiAskResponseDto ask(AiAskRequestDto requestDto) {
        return webClient.post()
                .uri("/ask")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(AiAskResponseDto.class)
                .block();
    }
}
