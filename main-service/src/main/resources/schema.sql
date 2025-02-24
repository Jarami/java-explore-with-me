DROP TABLE IF EXISTS categories CASCADE;
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT uq_category_name UNIQUE (name)
);
COMMENT ON TABLE categories IS 'Таблица категорий';
COMMENT ON COLUMN categories.id IS 'Идентификатор категории';
COMMENT ON COLUMN categories.name IS 'Название категории';

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL,
    CONSTRAINT uq_user_email UNIQUE (email)
);
COMMENT ON TABLE users IS 'Таблица пользователей';
COMMENT ON COLUMN users.id IS 'Идентификатор пользователя';
COMMENT ON COLUMN users.name IS 'Имя пользователя';
COMMENT ON COLUMN users.email IS 'Почта пользователя';

