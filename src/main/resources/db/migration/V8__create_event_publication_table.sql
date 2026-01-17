-- Spring Modulith Event Publication table for persistent event handling
CREATE TABLE IF NOT EXISTS event_publication
(
    id                    UUID                     NOT NULL PRIMARY KEY,
    event_type            VARCHAR(512)             NOT NULL,
    listener_id           VARCHAR(512)             NOT NULL,
    publication_date      TIMESTAMP WITH TIME ZONE NOT NULL,
    serialized_event      TEXT                     NOT NULL,
    status                VARCHAR(50)              NOT NULL DEFAULT 'PENDING',
    completion_attempts   INTEGER                  NOT NULL DEFAULT 0,
    last_resubmission_date TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_event_publication_status ON event_publication (status);
CREATE INDEX idx_event_publication_publication_date ON event_publication (publication_date);
