CREATE TABLE schedule_dates (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    property_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    entry_type VARCHAR(50) NOT NULL,
    is_cancelled BOOLEAN DEFAULT FALSE,
    guest_number INTEGER,
    guest_details VARCHAR(255),
    CONSTRAINT entry_type_check CHECK (entry_type IN ('BOOKING', 'BLOCK')),
    FOREIGN KEY (property_id) REFERENCES property(id)
);