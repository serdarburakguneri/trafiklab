CREATE TABLE journey_pattern
(
    id                BIGSERIAL NOT NULL PRIMARY KEY,
    line_number       VARCHAR(255),
    stop_point_number VARCHAR(255),
    direction         VARCHAR(255),
    exists_from_date  TIMESTAMP
);

CREATE TABLE stop_point
(
    id                BIGSERIAL NOT NULL PRIMARY KEY,
    stop_point_number VARCHAR(255),
    stop_point_name   VARCHAR(255),
    exists_from_date  TIMESTAMP
);

CREATE TABLE line
(
    id               BIGSERIAL NOT NULL PRIMARY KEY,
    line_number      VARCHAR(255),
    exists_from_date TIMESTAMP
);
