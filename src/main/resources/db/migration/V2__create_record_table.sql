CREATE TABLE record
(
  id                 BIGINT PRIMARY KEY,
  telegram_id        BIGINT                   NOT NULL REFERENCES "user" (telegram_id),
  creation_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  mood               INTEGER                  NOT NULL,
  sleep              INTEGER                  NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS user_id_seq INCREMENT 50 START 1;