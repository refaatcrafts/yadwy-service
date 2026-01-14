CREATE SCHEMA IF NOT EXISTS cart;

CREATE TABLE cart.carts
(
    id         BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_carts_account_id ON cart.carts (account_id);

CREATE TABLE cart.cart_items
(
    id         BIGSERIAL PRIMARY KEY,
    cart_id    BIGINT         NOT NULL REFERENCES cart.carts (id) ON DELETE CASCADE,
    product_id BIGINT         NOT NULL,
    quantity   INTEGER        NOT NULL CHECK (quantity >= 1),
    unit_price DECIMAL(19, 4) NOT NULL CHECK (unit_price >= 0),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (cart_id, product_id)
);

CREATE INDEX idx_cart_items_cart_id ON cart.cart_items (cart_id);
CREATE INDEX idx_cart_items_product_id ON cart.cart_items (product_id);
