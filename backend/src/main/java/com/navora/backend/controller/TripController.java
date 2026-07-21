package com.navora.backend.controller;

import com.navora.backend.dto.TripRequestDto;
import com.navora.backend.dto.TripResponseDto;
import com.navora.backend.service.AiServiceClient;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "http://localhost:5173")
@SecurityRequirement(name = "bearerAuth")
public class TripController {

    @Autowired
    private final AiServiceClient aiServiceClient;

    public TripController(AiServiceClient aiServiceClient) {
        this.aiServiceClient = aiServiceClient;
    }

    @PostMapping("/plan")
    public TripResponseDto planTrip(@RequestBody TripRequestDto request) {
        return aiServiceClient.generateTrip(request);
    }
}
