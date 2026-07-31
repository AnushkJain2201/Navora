package com.navora.backend.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeocodingServiceTest {
    @Test
    void geocodeKnownLocation() {
        GeocodingService service = new GeocodingService();

        var result = service.geocode("Great Pyramid of Giza, Egypt");

        assertTrue(result.isPresent());
        assertEquals(29.9792, result.get().latitude(), 0.5);
        assertEquals(31.1342, result.get().longitude(), 0.5);
    }
}
