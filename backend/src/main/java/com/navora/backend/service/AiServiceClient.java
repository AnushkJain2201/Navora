package com.navora.backend.service;

import com.navora.backend.dto.TripRequestDto;
import com.navora.backend.dto.TripResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AiServiceClient {

    private final WebClient webClient;

    public AiServiceClient(@Value("${ai.service.url}") String aiServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(aiServiceUrl)
                .build();
    }

    public TripResponseDto generateTrip(TripRequestDto request) {
        return webClient.post()
                .uri("/generate")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TripResponseDto.class)
                .block();
    }
}
