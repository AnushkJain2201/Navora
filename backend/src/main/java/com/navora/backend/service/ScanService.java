package com.navora.backend.service;

import com.navora.backend.dto.*;
import com.navora.backend.entity.Scan;
import com.navora.backend.entity.User;
import com.navora.backend.repository.ScanRepository;
import com.navora.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class ScanService {
    private final ImageStorageService imageStorageService;
    private final NearbyLandmarksService nearbyLandmarksService;
    private final VisionAiServiceClient visionAiServiceClient;
    private final ScanRepository scanRepository;
    private final UserRepository userRepository;

    public ScanService(ImageStorageService imageStorageService, NearbyLandmarksService nearbyLandmarksService, VisionAiServiceClient visionAiServiceClient, ScanRepository scanRepository, UserRepository userRepository) {
        this.imageStorageService = imageStorageService;
        this.nearbyLandmarksService = nearbyLandmarksService;
        this.visionAiServiceClient = visionAiServiceClient;
        this.scanRepository = scanRepository;
        this.userRepository = userRepository;
    }

    public ScanIdentifyResponseDto identify(String userEmail, MultipartFile image, double latitude, double longitude) throws Exception {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));

        String imageUrl = imageStorageService.uploadImage(image);

        List<NearbyLandmarkDto> nearby = nearbyLandmarksService.findNearby(latitude, longitude, 5);

        List<AiLandmarkCandidateDto> candidates = nearby.stream()
                .map(l -> new AiLandmarkCandidateDto(l.id().toString(), l.name(), l.category(), l.description()))
                .toList();

        String imageBase64 = Base64.getEncoder().encodeToString(image.getBytes());

        AiIdentifiedLandmarkDto aiResult = visionAiServiceClient.identify(
                new AiScanIdentifyRequestDto(imageBase64, candidates)
        );

        UUID matchedLandmarkId = aiResult.matched() && aiResult.landmarkId() != null
                ? UUID.fromString(aiResult.landmarkId())
                : null;

        Scan scan = new Scan(user, imageUrl, matchedLandmarkId, aiResult.matched(), aiResult.landmarkName(), aiResult.specificFeature(), aiResult.generatedContext());

        scanRepository.save(scan);

        return new ScanIdentifyResponseDto(
                scan.getId(),
                imageUrl,
                aiResult.matched(),
                aiResult.landmarkName(),
                aiResult.specificFeature(),
                aiResult.generatedContext()
        );
    }

    public List<ScanSummaryDto> getScansForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));

        return scanRepository.findByUserOrderByScannedAtDesc(user).stream()
                .map(scan -> new ScanSummaryDto(
                        scan.getId(),
                        scan.getImageUrl(),
                        scan.isMatched(),
                        scan.getLandmarkName(),
                        scan.getSpecificFeature(),
                        scan.getGeneratedContext(),
                        scan.getScannedAt()
                )).toList();
    }
}
