package com.navora.backend.controller;

import com.navora.backend.dto.NearbyLandmarkDto;
import com.navora.backend.dto.ScanIdentifyResponseDto;
import com.navora.backend.service.ImageStorageService;
import com.navora.backend.service.NearbyLandmarksService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scans")
@CrossOrigin(origins = "http://localhost:5173")
@SecurityRequirement(name = "bearerAuth")
public class ScanController {

    public final ImageStorageService imageStorageService;
    public final NearbyLandmarksService nearbyLandmarksService;

    public ScanController(ImageStorageService imageStorageService, NearbyLandmarksService nearbyLandmarksService) {
        this.imageStorageService = imageStorageService;
        this.nearbyLandmarksService = nearbyLandmarksService;
    }

    @PostMapping(value = "/identify", consumes = "multipart/form-data")
    public ResponseEntity<ScanIdentifyResponseDto> identify(
            @RequestParam("image") MultipartFile image,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude
            ) throws Exception {
        String imageUrl = imageStorageService.uploadImage(image);

        List<NearbyLandmarkDto> candidates = nearbyLandmarksService.findNearby(latitude, longitude, 5);

        ScanIdentifyResponseDto response = new ScanIdentifyResponseDto(
                UUID.randomUUID(),
                imageUrl,
                candidates
        );

        return ResponseEntity.ok(response);
    }
}
