package com.navora.backend.repository;

import com.navora.backend.entity.Landmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LandmarkRepository extends JpaRepository<Landmark, UUID> {
    @Query(value = """
            SELECT
                id,
                name,
                category,
                description,
                ST_Y(location::geometry) AS latitude,
                ST_X(location::geometry) AS longitude,
                ST_Distance(location, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography) AS distance_meters
            FROM landmarks
            ORDER BY location <-> ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography
            LIMIT :limit
            """, nativeQuery = true)
    List<Object[]> findNearbyLandmarksRaw(
            @Param("lat") double lat,
            @Param("lon") double lon,
            @Param("limit") int limit
    );
}
