CREATE TABLE IF NOT EXISTS temporary_record
(
  telegram_id        BIGINT PRIMARY KEY,
  creation_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  update_timestamp   TIMESTAMP WITH TIME ZONE,
  record             VARCHAR NOT NULL
);