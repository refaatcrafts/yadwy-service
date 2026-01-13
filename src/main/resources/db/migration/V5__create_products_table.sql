CREATE SCHEMA IF NOT EXISTS product;

CREATE SEQUENCE product.products_id_seq;

CREATE TABLE product.products
(
    id               BIGSERIAL PRIMARY KEY,
    seller_id        BIGINT         NOT NULL,
    name             JSONB          NOT NULL,
    description      JSONB,
    images           TEXT[]         NOT NULL DEFAULT '{}',
    price            DECIMAL(19, 4) NOT NULL,
    compare_at_price DECIMAL(19, 4),
    category_id      BIGINT         NOT NULL,
    stock            INTEGER        NOT NULL DEFAULT 0,
    track_inventory  BOOLEAN        NOT NULL DEFAULT true,
    visible          BOOLEAN        NOT NULL DEFAULT false,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_seller_id ON product.products (seller_id);
CREATE INDEX idx_products_category_id ON product.products (category_id);
CREATE INDEX idx_products_visible ON product.products (visible);
CREATE INDEX idx_products_seller_visible ON product.products (seller_id, visible);
