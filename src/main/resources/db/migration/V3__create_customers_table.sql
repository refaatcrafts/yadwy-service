CREATE SCHEMA IF NOT EXISTS customer;

CREATE TABLE customer.customers
(
    id            BIGSERIAL PRIMARY KEY,
    account_id    BIGINT       NOT NULL UNIQUE,
    customer_name VARCHAR(100) NOT NULL
);

CREATE INDEX idx_customers_account_id ON customer.customers(account_id);
