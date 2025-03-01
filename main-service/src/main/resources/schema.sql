DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS participations CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT uq_category_name UNIQUE (name)
);
COMMENT ON TABLE categories IS 'Таблица категорий событий';
COMMENT ON COLUMN categories.id IS 'Идентификатор категории';
COMMENT ON COLUMN categories.name IS 'Название категории';


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

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL,
    initiator_id BIGINT NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    location_lat REAL NOT NULL,
    location_lon REAL NOT NULL,
    paid BOOLEAN NOT NULL DEFAULT false,
    participant_limit INTEGER NOT NULL DEFAULT 0,
    request_moderation BOOLEAN NOT NULL DEFAULT true,
    state VARCHAR NOT NULL DEFAULT 'PENDING',
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT events_category_id_fk
        FOREIGN KEY(category_id)
            REFERENCES categories(id)
                ON DELETE RESTRICT,

    CONSTRAINT events_initiator_id_fk
        FOREIGN KEY(initiator_id)
            REFERENCES users(id)
                ON DELETE CASCADE,

    CONSTRAINT events_title_limits
        CHECK (3 <= length(title) AND length(title) <= 120),

    CONSTRAINT events_annotation_limits
        CHECK (20 <= length(annotation) AND length(annotation) <= 2000),

    CONSTRAINT events_description_limits
        CHECK (20 <= length(description) AND length(description) <= 7000),

    CONSTRAINT events_location_lat_limits
        CHECK (-90 <= location_lat AND location_lat <= 90),

    CONSTRAINT events_location_lon_limits
        CHECK (-180 <= location_lon AND location_lon < 180),

    CONSTRAINT events_participant_limit_limits
        CHECK (participant_limit >= 0),

    CONSTRAINT events_state_values
        CHECK (state IN ('PENDING', 'PUBLISHED', 'CANCELED'))
);
COMMENT ON TABLE events IS 'Таблица событий';
COMMENT ON COLUMN events.id IS 'Идентификатор события';
COMMENT ON COLUMN events.title IS 'Заголовок события';
COMMENT ON COLUMN events.annotation IS 'Краткое описание события';
COMMENT ON COLUMN events.category_id IS 'Идентификатор категории события';
COMMENT ON COLUMN events.initiator_id IS 'Идентификатор инициатора события';
COMMENT ON COLUMN events.description IS 'Полное описание события';
COMMENT ON COLUMN events.event_date IS 'Дата и время на которые намечено событие';
COMMENT ON COLUMN events.location_lat IS 'Широта места проведения события';
COMMENT ON COLUMN events.location_lon IS 'Долгота места проведения события';
COMMENT ON COLUMN events.paid IS 'Нужно ли оплачивать участие в событии';
COMMENT ON COLUMN events.state IS 'Состояние события (PENDING, PUBLISHED, CANCELED)';
COMMENT ON COLUMN events.participant_limit IS 'Ограничение на количество участников. Значение 0 - означает отсутствие ограничения';
COMMENT ON COLUMN events.request_moderation IS 'Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.';
COMMENT ON COLUMN events.created_on IS 'Дата-время создания события';
COMMENT ON COLUMN events.published_on IS 'Дата-время публикации события';

CREATE TABLE participations (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT participations_requester_fk
        FOREIGN KEY(requester_id)
            REFERENCES users(id)
                ON DELETE CASCADE,

    CONSTRAINT participations_event_fk
        FOREIGN KEY(event_id)
            REFERENCES events(id)
                ON DELETE CASCADE,

    CONSTRAINT participations_status_values
        CHECK (status IN ('PENDING', 'CONFIRMED', 'REJECTED', 'CANCELED')),

    CONSTRAINT participations_unique UNIQUE (event_id, requester_id)
);
COMMENT ON TABLE participations IS 'Таблица заявок на участие в событиях';
COMMENT ON COLUMN participations.id IS 'Идентификатор заявки';
COMMENT ON COLUMN participations.event_id IS 'Идентификатор события';
COMMENT ON COLUMN participations.requester_id IS 'Идентификатор пользователя, отправившего заявку';
COMMENT ON COLUMN participations.status IS 'Статус заявки';
COMMENT ON COLUMN participations.created_on IS 'Дата-время создания заявки';

CREATE TABLE compilations (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    pinned BOOLEAN NOT NULL DEFAULT false,

    CONSTRAINT events_title_limits
        CHECK (length(title) >= 1),

    CONSTRAINT compilations_title_unq
        UNIQUE (title)
);
COMMENT ON TABLE compilations IS 'Таблица подборок событий';
COMMENT ON COLUMN compilations.id IS 'Идентификатор подборки';
COMMENT ON COLUMN compilations.title IS 'Название подборки';
COMMENT ON COLUMN compilations.pinned IS 'Закреплена ли подборка на главной странице сайта';

CREATE TABLE compilations_events (
    compilation_id BIGINT,
    events_id BIGINT,

    CONSTRAINT compilations_events_compilation_fk
        FOREIGN KEY(compilation_id)
            REFERENCES compilations(id)
                ON DELETE CASCADE,

    CONSTRAINT compilations_events_event_fk
        FOREIGN KEY(events_id)
            REFERENCES events(id)
                ON DELETE CASCADE
);
COMMENT ON TABLE compilations_events IS 'Ассоциативная таблица событий и подборок событий';