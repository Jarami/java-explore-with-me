drop table if exists hits;
create table hits (
    id BIGSERIAL PRIMARY KEY,
    app VARCHAR NOT NULL,
    uri VARCHAR NOT NULL,
    ip VARCHAR NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);