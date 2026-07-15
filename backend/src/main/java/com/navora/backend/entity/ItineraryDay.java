package com.navora.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "itinerary_days")
public class ItineraryDay {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Column(name = "visit_date")
    private LocalDate visitDate;

    @OneToMany(mappedBy = "itineraryDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItineraryStop> stops = new ArrayList<>();

    protected ItineraryDay() {}

    public ItineraryDay(Trip trip, Integer dayNumber, LocalDate visitDate) {
        this.trip = trip;
        this.dayNumber = dayNumber;
        this.visitDate = visitDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public List<ItineraryStop> getStops() {
        return stops;
    }

    public void setStops(List<ItineraryStop> stops) {
        this.stops = stops;
    }
}
