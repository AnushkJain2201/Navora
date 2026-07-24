package com.navora.backend.service;

import com.navora.backend.dto.TripRequestDto;
import com.navora.backend.dto.TripResponseDto;
import com.navora.backend.dto.TripSummaryDto;
import com.navora.backend.entity.Trip;
import com.navora.backend.entity.User;
import com.navora.backend.repository.TripRepository;
import com.navora.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TripService {
    private final AiServiceClient aiServiceClient;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;

    public TripService(AiServiceClient aiServiceClient, UserRepository userRepository, TripRepository tripRepository) {
        this.aiServiceClient = aiServiceClient;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
    }

    public TripResponseDto planTrip(String userEmail, TripRequestDto tripRequestDto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found."));

        TripResponseDto aiResponse = aiServiceClient.generateTrip(tripRequestDto);

        // Placeholder values until the Phase 3 parses structured fields from the AI response

        Trip trip = new Trip(user, "Unknown", 0, BigDecimal.ZERO);
        tripRepository.save(trip);

        return aiResponse;
    }

    public List<TripSummaryDto> getTripsForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated use not found."));

        return tripRepository.findByUser(user).stream()
                .map(trip -> new TripSummaryDto(trip.getId(), trip.getCountry(), trip.getDurationDays(), trip.getBudget(), trip.getCreatedAt()))
                .toList();
    }
}
