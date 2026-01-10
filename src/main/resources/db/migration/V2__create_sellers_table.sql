CREATE SCHEMA IF NOT EXISTS seller;

CREATE TABLE seller.sellers
(
    id         BIGSERIAL PRIMARY KEY,
    account_id BIGINT       NOT NULL UNIQUE,
    store_name VARCHAR(100) NOT NULL
);

CREATE INDEX idx_sellers_account_id ON seller.sellers(account_id);
