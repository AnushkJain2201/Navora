package com.navora.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scans")
public class Scan {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "landmark_id")
    private UUID landmarkId; // nullable — a scan might not match a landmark

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "scanned_at", nullable = false, updatable = false)
    private LocalDateTime scannedAt = LocalDateTime.now();

    protected Scan() {}

    public Scan(User user, String imageUrl) {
        this.user = user;
        this.imageUrl = imageUrl;
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

    public LocalDateTime getScannedAt() {
        return scannedAt;
    }

    public void setScannedAt(LocalDateTime scannedAt) {
        this.scannedAt = scannedAt;
    }
}
