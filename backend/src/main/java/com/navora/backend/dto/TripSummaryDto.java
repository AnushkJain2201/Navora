package com.navora.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TripSummaryDto(UUID id, String country, Integer durationDays, BigDecimal budget, LocalDateTime createdAt) {
}
