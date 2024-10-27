CREATE TABLE IF NOT EXISTS metric
(
  id            BIGINT PRIMARY KEY,
  name          VARCHAR(255) NOT NULL,
  description   TEXT         NOT NULL,
  min_value     INTEGER      NOT NULL,
  max_value     INTEGER      NOT NULL,
  labels        JSONB,
  default_value INTEGER
);

CREATE SEQUENCE IF NOT EXISTS metric_id_seq INCREMENT 50 START 1;