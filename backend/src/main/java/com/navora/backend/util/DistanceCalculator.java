package com.navora.backend.util;

import com.navora.backend.dto.GeoCoordinateDto;

public class DistanceCalculator {
    private static final double EARTH_RADIUS_KM = 6371.0;

    public static double calculateDistanceKm(GeoCoordinateDto point1, GeoCoordinateDto point2) {
        double lat1Rad = Math.toRadians(point1.latitude());
        double lat2Rad = Math.toRadians(point2.latitude());
        double deltaLatRad = Math.toRadians(point2.latitude() - point1.latitude());
        double deltaLonRad = Math.toRadians(point2.longitude() - point1.longitude());

        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
