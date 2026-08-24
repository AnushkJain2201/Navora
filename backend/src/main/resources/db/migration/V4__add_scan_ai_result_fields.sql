ALTER TABLE scans
    ADD COLUMN matched BOOLEAN NOT NULL DEFAULT false,
    ADD COLUMN landmark_name VARCHAR(255),
    ADD COLUMN specific_feature VARCHAR(255),
    ADD COLUMN generated_context TEXT;
