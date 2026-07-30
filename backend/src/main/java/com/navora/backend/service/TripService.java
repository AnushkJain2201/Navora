package com.navora.backend.service;

import com.navora.backend.dto.*;
import com.navora.backend.entity.ItineraryDay;
import com.navora.backend.entity.ItineraryStop;
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

    public TripPlanResultDto planTrip(String userEmail, TripRequestDto tripRequestDto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found."));

        AiTripPlanResponseDto aiResponse = aiServiceClient.generateTrip(tripRequestDto);

        if(aiResponse.itinerary() == null) {
            return new TripPlanResultDto(null, null, null, null, null, aiResponse.clarificationMessage());
        }

        Trip trip = new Trip(
                user,
                aiResponse.destination(),
                aiResponse.durationDays(),
                BigDecimal.valueOf(aiResponse.budget())
        );

        for (AiItineraryDayDto dayDto: aiResponse.itinerary()) {
            ItineraryDay day = new ItineraryDay(trip, dayDto.dayNumber(), null);

            int orderIndex = 0;
            for (AiItineraryStopDto stopDto: dayDto.stops()) {
                ItineraryStop stop = new ItineraryStop(
                        day,
                        stopDto.name(),
                        stopDto.description(),
                        stopDto.estimatedDurationHours() != null
                                ? BigDecimal.valueOf(stopDto.estimatedDurationHours())
                                : null,
                        orderIndex++
                );
                day.getStops().add(stop);
            }

            trip.getItineraryDays().add(day);
        }

        tripRepository.save(trip);

        return toResultDto(trip);
    }

    private TripPlanResultDto toResultDto(Trip trip) {
        List<ItineraryDayDto> dayDtos = trip.getItineraryDays().stream()
                .map(day -> new ItineraryDayDto(
                        day.getId(),
                        day.getDayNumber(),
                        day.getStops().stream()
                                .map(stop -> new ItineraryStopDto(
                                        stop.getId(),
                                        stop.getLandmarkName(),
                                        stop.getDescription(),
                                        stop.getEstimatedDurationHours(),
                                        stop.getOrderIndex()))
                                .toList()))
                .toList();

        return new TripPlanResultDto(
                trip.getId(),
                trip.getCountry(),
                trip.getDurationDays(),
                trip.getBudget(),
                dayDtos,
                null
        );
    }

    public List<TripSummaryDto> getTripsForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated use not found."));

        return tripRepository.findByUser(user).stream()
                .map(trip -> new TripSummaryDto(trip.getId(), trip.getCountry(), trip.getDurationDays(), trip.getBudget(), trip.getCreatedAt()))
                .toList();
    }
}
