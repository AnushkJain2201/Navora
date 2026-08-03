package com.navora.backend.util;

import com.navora.backend.dto.GeoCoordinateDto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DistanceCalculatorTest {
    @Test
    void calculatesKnownDistanceBetweenCairoLandmarks() {
        GeoCoordinateDto pyramid = new GeoCoordinateDto(29.9792, 31.1342);
        GeoCoordinateDto museum = new GeoCoordinateDto(30.0478, 31.2336);

        double distanceKm = DistanceCalculator.calculateDistanceKm(pyramid, museum);

        assertEquals(12.0, distanceKm, 2.0); // roughly 10-14 km apart, allowing some tolerance
    }

    @Test
    void distanceBetweenSamePointIsZero() {
        GeoCoordinateDto point = new GeoCoordinateDto(29.9792, 31.1342);

        double distanceKm = DistanceCalculator.calculateDistanceKm(point, point);

        assertEquals(0.0, distanceKm, 0.001);
    }
}
