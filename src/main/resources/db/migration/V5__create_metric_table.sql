CREATE TABLE IF NOT EXISTS metric
(
  id            BIGINT,
  owner_id      INTEGER      NOT NULL,
  name          VARCHAR(255) NOT NULL,
  description   TEXT         NOT NULL,
  min_value     INTEGER      NOT NULL,
  max_value     INTEGER      NOT NULL,
  labels        JSONB,
  default_value INTEGER,
  PRIMARY KEY (owner_id, name)
);

CREATE SEQUENCE IF NOT EXISTS metric_id_seq INCREMENT 50 START 1;