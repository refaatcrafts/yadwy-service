CREATE SCHEMA IF NOT EXISTS identity;

CREATE TABLE identity.accounts
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50)  NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    roles TEXT[] NOT NULL DEFAULT '{}'
);