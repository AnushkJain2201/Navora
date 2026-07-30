ALTER TABLE itinerary_stops
    ALTER COLUMN landmark_id DROP NOT NULL;

ALTER TABLE itinerary_stops
    ADD COLUMN landmark_name VARCHAR(255) NOT NULL DEFAULT '',
    ADD COLUMN description TEXT,
    ADD COLUMN estimated_duration_hours NUMERIC(4, 1);