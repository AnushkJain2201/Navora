package com.navora.backend.service;

import com.navora.backend.dto.NearbyLandmarkDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class NearbyLandmarksServiceTest {

    @Autowired
    private NearbyLandmarksService nearbyLandmarksService;

    @Test
    void findsAmberFortNearJaipurCoordinates() {
        List<NearbyLandmarkDto> results = nearbyLandmarksService.findNearby(26.9855, 75.8513, 5);

        assertFalse(results.isEmpty());
        assertTrue(results.get(0).distanceMeters() < 2000);

        boolean foundAmberFort = results.stream()
                .anyMatch(r -> r.name().toLowerCase().contains("amber"));
        assertTrue(foundAmberFort, "Expected Amber Fort in nearby results.");

    }

}
