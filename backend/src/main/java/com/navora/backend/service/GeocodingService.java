package com.navora.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.navora.backend.dto.GeoCoordinateDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GeocodingService {
    private final WebClient webClient;
    private final ConcurrentHashMap<String, GeoCoordinateDto> cache = new ConcurrentHashMap<>();

    public GeocodingService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader("User-Agent", "NavoraTravelApp/1.0")
                .build();
    }

    public Optional<GeoCoordinateDto> geocode(String placeName) {
        if(cache.containsKey(placeName)) {
            return Optional.of(cache.get(placeName));
        }

        try {
            JsonNode response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("q", placeName)
                            .queryParam("format", "json")
                            .queryParam("limit", 1)
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if(response == null || !response.isArray() || response.isEmpty()) {
                return Optional.empty();
            }

            JsonNode firstResult = response.get(0);

            double lat = firstResult.get("lat").asDouble();
            double lon = firstResult.get("lon").asDouble();

            GeoCoordinateDto coordinate = new GeoCoordinateDto(lat, lon);
            cache.put(placeName, coordinate);

            Thread.sleep(1000); // respect Nominatim's 1 request/second rate limit

            return Optional.of(coordinate);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
