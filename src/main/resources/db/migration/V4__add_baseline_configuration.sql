DROP TABLE "user";

CREATE TABLE IF NOT EXISTS configuration
(
  id                    BIGINT PRIMARY KEY,
  baseline_metrics      JSONB,
  auto_baseline_enabled BOOLEAN          DEFAULT FALSE,
  notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE SEQUENCE IF NOT EXISTS configuration_id_seq INCREMENT 50 START 1;

CREATE TABLE IF NOT EXISTS "user"
(
  id               BIGINT PRIMARY KEY,
  telegram_id      BIGINT UNIQUE NOT NULL,
  configuration_id BIGINT        NOT NULL REFERENCES configuration (id)
)