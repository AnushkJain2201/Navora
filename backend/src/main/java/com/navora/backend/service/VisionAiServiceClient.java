package com.navora.backend.service;

import com.navora.backend.dto.AiIdentifiedLandmarkDto;
import com.navora.backend.dto.AiScanIdentifyRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class VisionAiServiceClient {
    private final WebClient webClient;

    public VisionAiServiceClient(WebClient.Builder webClientBuilder, @Value("${ai.service.url}") String aiServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(aiServiceUrl).build();
    }

    public AiIdentifiedLandmarkDto identify(AiScanIdentifyRequestDto requestDto) {
        return webClient.post()
                .uri("/identify")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(AiIdentifiedLandmarkDto.class)
                .block();
    }
}
