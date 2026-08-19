package com.navora.backend.service;

import com.navora.backend.dto.NearbyLandmarkDto;
import com.navora.backend.repository.LandmarkRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class NearbyLandmarksService {
    private final LandmarkRepository landmarkRepository;

    public NearbyLandmarksService(LandmarkRepository landmarkRepository) {
        this.landmarkRepository = landmarkRepository;
    }

    public List<NearbyLandmarkDto> findNearby(double latitude, double longitude, int limit) {
        List<Object[]> rawResults = landmarkRepository.findNearbyLandmarksRaw(latitude, longitude, limit);

        return rawResults.stream()
                .map(this::mapToDto)
                .toList();
    }

    private NearbyLandmarkDto mapToDto(Object[] row) {
        return new NearbyLandmarkDto(
                (UUID) row[0],
                (String) row[1],
                (String) row[2],
                (String) row[3],
                BigDecimal.valueOf((Double) row[4]),
                BigDecimal.valueOf((Double) row[5]),
                (Double) row[6]
        );
    }
}
