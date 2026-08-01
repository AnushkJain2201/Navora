package com.navora.backend.service;

import com.navora.backend.dto.GeocodedStopDto;
import com.navora.backend.util.DistanceCalculator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteOptimizerService {
    public List<GeocodedStopDto> nearestNeighbourRoute(List<GeocodedStopDto> stops) {
        if(stops.size() <= 2) {
            return stops;
        }

        List<GeocodedStopDto> unvisited = new ArrayList<>(stops);
        List<GeocodedStopDto> route = new ArrayList<>();

        GeocodedStopDto current = unvisited.remove(0);
        route.add(current);

        while(!unvisited.isEmpty()) {
            GeocodedStopDto nearest = findNearest(current, unvisited);
            unvisited.remove(nearest);
            route.add(nearest);
            current = nearest;
        }

        return route;
    }

    private GeocodedStopDto findNearest(GeocodedStopDto from, List<GeocodedStopDto> candidates) {
        GeocodedStopDto nearest = null;
        double shortestDistance = Double.MAX_VALUE;

        for (GeocodedStopDto candidate : candidates) {
            double distance = DistanceCalculator.calculateDistanceKm(from.coordinate(), candidate.coordinate());
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearest = candidate;
            }
        }

        return nearest;
    }
}
