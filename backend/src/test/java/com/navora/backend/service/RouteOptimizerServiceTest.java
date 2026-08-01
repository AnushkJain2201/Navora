package com.navora.backend.service;

import com.navora.backend.dto.GeoCoordinateDto;
import com.navora.backend.dto.GeocodedStopDto;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RouteOptimizerServiceTest {

    @Test
    void nearestNeighborVisitsClosestStopsFirst() {
        RouteOptimizerService optimizer = new RouteOptimizerService();

        // Points roughly in a line: A at 0, B at 1, C at 2, D at 10 (units are degrees, simplified)
        GeocodedStopDto a = new GeocodedStopDto(0, "A", new GeoCoordinateDto(0.0, 0.0));
        GeocodedStopDto b = new GeocodedStopDto(1, "B", new GeoCoordinateDto(0.01, 0.0));
        GeocodedStopDto c = new GeocodedStopDto(2, "C", new GeoCoordinateDto(0.02, 0.0));
        GeocodedStopDto d = new GeocodedStopDto(3, "D", new GeoCoordinateDto(0.10, 0.0));

        // Deliberately out of order input: A, D, B, C
        List<GeocodedStopDto> input = List.of(a, d, b, c);

        List<GeocodedStopDto> result = optimizer.nearestNeighbourRoute(input);

        // Starting at A (first in list), nearest neighbor should visit B, then C, then D
        assertEquals(List.of(a, b, c, d), result);
    }
}
