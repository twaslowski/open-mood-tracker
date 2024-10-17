CREATE TABLE record
(
  id                 BIGINT PRIMARY KEY,
  telegram_id        BIGINT                   NOT NULL,
  creation_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  values             JSONB                    NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS user_id_seq INCREMENT 50 START 1;