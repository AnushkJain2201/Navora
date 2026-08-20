package com.navora.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scans")
public class Scan {

    @Id
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "landmark_id")
    private UUID landmarkId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private boolean matched;

    @Column(name = "landmark_name")
    private String landmarkName;

    @Column(name = "specific_feature")
    private String specificFeature;

    @Column(name = "generated_context", columnDefinition = "TEXT")
    private String generatedContext;

    @Column(name = "scanned_at", nullable = false, updatable = false)
    private LocalDateTime scannedAt = LocalDateTime.now();

    protected Scan() {}

    public Scan(User user, String imageUrl, UUID landmarkId, boolean matched,
                String landmarkName, String specificFeature, String generatedContext) {
        this.user = user;
        this.imageUrl = imageUrl;
        this.landmarkId = landmarkId;
        this.matched = matched;
        this.landmarkName = landmarkName;
        this.specificFeature = specificFeature;
        this.generatedContext = generatedContext;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getLandmarkId() {
        return landmarkId;
    }

    public void setLandmarkId(UUID landmarkId) {
        this.landmarkId = landmarkId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public String getLandmarkName() {
        return landmarkName;
    }

    public void setLandmarkName(String landmarkName) {
        this.landmarkName = landmarkName;
    }

    public String getSpecificFeature() {
        return specificFeature;
    }

    public void setSpecificFeature(String specificFeature) {
        this.specificFeature = specificFeature;
    }

    public String getGeneratedContext() {
        return generatedContext;
    }

    public void setGeneratedContext(String generatedContext) {
        this.generatedContext = generatedContext;
    }

    public LocalDateTime getScannedAt() {
        return scannedAt;
    }

    public void setScannedAt(LocalDateTime scannedAt) {
        this.scannedAt = scannedAt;
    }
}
