CREATE SCHEMA IF NOT EXISTS category;

CREATE SEQUENCE category.categories_id_seq;

CREATE TABLE category.categories
(
    id          BIGSERIAL PRIMARY KEY,
    name        JSONB        NOT NULL,
    slug        VARCHAR(100) NOT NULL UNIQUE,
    image_url   VARCHAR(500),
    description JSONB,
    parent_id   BIGINT REFERENCES category.categories (id)
);

CREATE INDEX idx_categories_parent_id ON category.categories (parent_id);
CREATE INDEX idx_categories_slug ON category.categories (slug);
