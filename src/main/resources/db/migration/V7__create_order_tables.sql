CREATE SCHEMA IF NOT EXISTS "order";

CREATE TABLE "order".orders
(
    id             BIGSERIAL PRIMARY KEY,
    account_id     BIGINT                   NOT NULL,
    status         VARCHAR(20)              NOT NULL DEFAULT 'RECEIVED',
    recipient_name VARCHAR(100)             NOT NULL,
    street         VARCHAR(255)             NOT NULL,
    city           VARCHAR(100)             NOT NULL,
    governorate    VARCHAR(100)             NOT NULL,
    phone          VARCHAR(20)              NOT NULL,
    notes          TEXT,
    payment_method VARCHAR(20)              NOT NULL DEFAULT 'COD',
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_account_id ON "order".orders (account_id);
CREATE INDEX idx_orders_status ON "order".orders (status);

CREATE TABLE "order".seller_orders
(
    id         BIGSERIAL PRIMARY KEY,
    order_id   BIGINT      NOT NULL REFERENCES "order".orders (id) ON DELETE CASCADE,
    seller_id  BIGINT      NOT NULL,
    status     VARCHAR(20) NOT NULL DEFAULT 'RECEIVED',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_seller_orders_order_id ON "order".seller_orders (order_id);
CREATE INDEX idx_seller_orders_seller_id ON "order".seller_orders (seller_id);
CREATE INDEX idx_seller_orders_status ON "order".seller_orders (status);

CREATE TABLE "order".order_lines
(
    id              BIGSERIAL PRIMARY KEY,
    seller_order_id BIGINT         NOT NULL REFERENCES "order".seller_orders (id) ON DELETE CASCADE,
    product_id      BIGINT         NOT NULL,
    product_name    JSONB          NOT NULL,
    unit_price      DECIMAL(19, 4) NOT NULL CHECK (unit_price >= 0),
    quantity        INTEGER        NOT NULL CHECK (quantity >= 1),
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_order_lines_seller_order_id ON "order".order_lines (seller_order_id);
CREATE INDEX idx_order_lines_product_id ON "order".order_lines (product_id);
