package com.navora.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// Our Python service returns raw_response (snake_case), but Java convention is rawResponse (camelCase). Jackson (Spring's JSON library) won't match these automatically. Fix it with an annotation:
public record TripResponseDto(@JsonProperty("raw_response") String rawResponse) {
}
