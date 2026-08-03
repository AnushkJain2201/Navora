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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {
    private final AiServiceClient aiServiceClient;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final GeocodingService geocodingService;
    private final RouteOptimizerService routeOptimizerService;

    public TripService(AiServiceClient aiServiceClient, UserRepository userRepository, TripRepository tripRepository, GeocodingService geocodingService, RouteOptimizerService routeOptimizerService) {
        this.aiServiceClient = aiServiceClient;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.geocodingService = geocodingService;
        this.routeOptimizerService = routeOptimizerService;
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

            List<ItineraryStop> orderedStops = buildOptimizedStops(day, dayDto, aiResponse.destination());

            day.getStops().addAll(orderedStops);

            trip.getItineraryDays().add(day);
        }

        tripRepository.save(trip);

        return toResultDto(trip);
    }

    private List<ItineraryStop> buildOptimizedStops(ItineraryDay day, AiItineraryDayDto dayDto, String destination) {
        List<GeocodedStopDto> geocodedStops = new ArrayList<>();

        for (int i = 0; i < dayDto.stops().size(); i++) {
            final int stopIndex = i;

            AiItineraryStopDto stopDto = dayDto.stops().get(i);
            Optional<GeoCoordinateDto> coordinate =
                    geocodingService.geocode(stopDto.name() + ", " + destination);

            coordinate.ifPresent(coord ->
                    geocodedStops.add(new GeocodedStopDto(stopIndex, stopDto.name(), coord)));
        }
        List<ItineraryStop> stops = new ArrayList<>();

        if (geocodedStops.size() == dayDto.stops().size() && geocodedStops.size() > 1) {
            // Every stop geocoded successfully — safe to optimize
            List<GeocodedStopDto> optimizedOrder = routeOptimizerService.optimizeRoute(geocodedStops);

            int orderIndex = 0;
            for (GeocodedStopDto geocodedStop : optimizedOrder) {
                AiItineraryStopDto original = dayDto.stops().get(geocodedStop.originalIndex());
                stops.add(buildStop(day, original, orderIndex++));
            }
        } else {
            // One or more stops failed to geocode — fall back to the AI's original order
            int orderIndex = 0;
            for (AiItineraryStopDto stopDto : dayDto.stops()) {
                stops.add(buildStop(day, stopDto, orderIndex++));
            }
        }

        return stops;
    }

    private ItineraryStop buildStop(ItineraryDay day, AiItineraryStopDto stopDto, int orderIndex) {
        return new ItineraryStop(
                day,
                stopDto.name(),
                stopDto.description(),
                stopDto.estimatedDurationHours() != null
                        ? BigDecimal.valueOf(stopDto.estimatedDurationHours())
                        : null,
                orderIndex
        );
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
