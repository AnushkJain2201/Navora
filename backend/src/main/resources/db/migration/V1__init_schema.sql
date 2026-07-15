CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE trips (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    country VARCHAR(100) NOT NULL,
    duration_days INT NOT NULL,
    budget NUMERIC(12, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE itinerary_days (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    trip_id UUID NOT NULL REFERENCES trips(id) ON DELETE CASCADE,
    day_number INT NOT NULL,
    visit_date DATE
);

CREATE TABLE landmarks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100) NOT NULL,
    location GEOGRAPHY(POINT, 4326) NOT NULL,
    category VARCHAR(100),
    description TEXT
);

CREATE TABLE itinerary_stops (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    itinerary_day_id UUID NOT NULL REFERENCES itinerary_days(id) ON DELETE CASCADE,
    landmark_id UUID NOT NULL REFERENCES landmarks(id),
    order_index INT NOT NULL,
    arrival_time TIME
);

CREATE TABLE landmark_embeddings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    landmark_id UUID NOT NULL REFERENCES landmarks(id) ON DELETE CASCADE,
    embedding VECTOR(1536)
);

CREATE TABLE scans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    landmark_id UUID REFERENCES landmarks(id),
    image_url VARCHAR(500),
    scanned_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_landmarks_location ON landmarks USING GIST (location);
CREATE INDEX idx_trips_user_id ON trips(user_id);
CREATE INDEX idx_scans_user_id ON scans(user_id);