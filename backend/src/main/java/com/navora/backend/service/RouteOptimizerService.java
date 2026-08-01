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

    private double calculateTotalDistance(List<GeocodedStopDto> route) {
        double total = 0.0;

        for(int i = 0; i < route.size() - 1; i++) {
            total += DistanceCalculator.calculateDistanceKm(route.get(i).coordinate(), route.get(i + 1).coordinate());
        }

        return total;
    }

    double calculateTotalDistanceForTest(List<GeocodedStopDto> route) {
        return calculateTotalDistance(route);
    }

    public List<GeocodedStopDto> twoOptImprove(List<GeocodedStopDto> route) {
        if(route.size() <= 3) {
            return route;
        }

        List<GeocodedStopDto> bestRoute = new ArrayList<>(route);
        boolean improved = true;

        while (improved) {
            improved = false;
            double bestDistance = calculateTotalDistance(bestRoute);

            for (int i = 1; i < bestRoute.size() - 1; i++) {
                for (int j = i + 1; j < bestRoute.size(); j++) {
                    List<GeocodedStopDto> candidate = twoOptSwap(bestRoute, i, j);
                    double candidateDistance = calculateTotalDistance(candidate);

                    if (candidateDistance < bestDistance) {
                        bestRoute = candidate;
                        bestDistance = candidateDistance;
                        improved = true;
                    }
                }
            }
        }

        return bestRoute;
    }

    private List<GeocodedStopDto> twoOptSwap(List<GeocodedStopDto> route, int i, int j) {
        List<GeocodedStopDto> newRoute = new ArrayList<>(route.subList(0, i));
        List<GeocodedStopDto> reversedSegment = new ArrayList<>(route.subList(i, j + 1));
        java.util.Collections.reverse(reversedSegment);
        newRoute.addAll(reversedSegment);
        newRoute.addAll(route.subList(j + 1, route.size()));
        return newRoute;
    }
}
