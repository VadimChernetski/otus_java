CREATE TABLE users (
    id BIGSERIAL UNIQUE PRIMARY KEY  ,
    email VARCHAR,
    nickname VARCHAR,
    password VARCHAR
);

CREATE TABLE chat (
    id BIGSERIAL UNIQUE PRIMARY KEY ,
    first_user BIGINT REFERENCES users (id),
    second_user BIGINT REFERENCES users (id)
);

CREATE TABLE message (
    id BIGSERIAL UNIQUE PRIMARY KEY ,
    author_id BIGINT REFERENCES users (id),
    chat_id BIGINT REFERENCES chat (id),
    text VARCHAR,
    created_timestamp TIMESTAMPTZ
);
