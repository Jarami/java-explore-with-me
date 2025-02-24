DROP TABLE IF EXISTS categories CASCADE;
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT uq_category_name UNIQUE (name)
);
COMMENT ON TABLE categories IS 'Таблица категорий';
COMMENT ON COLUMN categories.id IS 'Идентификатор категории';