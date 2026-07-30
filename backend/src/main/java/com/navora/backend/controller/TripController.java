package com.navora.backend.controller;

import com.navora.backend.dto.TripPlanResultDto;
import com.navora.backend.dto.TripRequestDto;
import com.navora.backend.dto.TripSummaryDto;
import com.navora.backend.service.TripService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "http://localhost:5173")
@SecurityRequirement(name = "bearerAuth")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/plan")
    public ResponseEntity<TripPlanResultDto> planTrip(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TripRequestDto request
    ) {
        TripPlanResultDto response = tripService.planTrip(userDetails.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TripSummaryDto>> getTripsByUser(@AuthenticationPrincipal UserDetails userDetails) {
        List<TripSummaryDto> trips = tripService.getTripsForUser(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(trips);
    }
}
