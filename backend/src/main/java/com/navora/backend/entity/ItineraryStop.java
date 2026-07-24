package com.navora.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "itinerary_stops")
public class ItineraryStop {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_day_id", nullable = false)
    private ItineraryDay itineraryDay;

    // Landmark entity comes in Phase 6 — for now we store the raw FK value
    @Column(name = "landmark_id", nullable = false)
    private UUID landmarkId;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;

    protected ItineraryStop() {}

    public ItineraryStop(ItineraryDay itineraryDay, UUID landmarkId, Integer orderIndex, LocalTime arrivalTime) {
        this.itineraryDay = itineraryDay;
        this.landmarkId = landmarkId;
        this.orderIndex = orderIndex;
        this.arrivalTime = arrivalTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ItineraryDay getItineraryDay() {
        return itineraryDay;
    }

    public void setItineraryDay(ItineraryDay itineraryDay) {
        this.itineraryDay = itineraryDay;
    }

    public UUID getLandmarkId() {
        return landmarkId;
    }

    public void setLandmarkId(UUID landmarkId) {
        this.landmarkId = landmarkId;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
