package com.navora.backend.controller;

import com.navora.backend.dto.ScanIdentifyResponseDto;
import com.navora.backend.dto.ScanSummaryDto;
import com.navora.backend.service.ImageStorageService;
import com.navora.backend.service.NearbyLandmarksService;
import com.navora.backend.service.ScanService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/scans")
@CrossOrigin(origins = "http://localhost:5173")
@SecurityRequirement(name = "bearerAuth")
public class ScanController {

    public final ImageStorageService imageStorageService;
    public final NearbyLandmarksService nearbyLandmarksService;
    public final ScanService scanService;

    public ScanController(ImageStorageService imageStorageService, NearbyLandmarksService nearbyLandmarksService, ScanService scanService) {
        this.imageStorageService = imageStorageService;
        this.nearbyLandmarksService = nearbyLandmarksService;
        this.scanService = scanService;
    }

    @PostMapping(value = "/identify", consumes = "multipart/form-data")
    public ResponseEntity<ScanIdentifyResponseDto> identify(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("image") MultipartFile image,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude
            ) throws Exception {
        ScanIdentifyResponseDto responseDto = scanService.identify(userDetails.getUsername(), image, latitude, longitude);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ScanSummaryDto>> getMyScans(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(scanService.getScansForUser(userDetails.getUsername()));
    }
}
