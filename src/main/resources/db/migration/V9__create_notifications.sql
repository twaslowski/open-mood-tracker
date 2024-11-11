CREATE TABLE IF NOT EXISTS notification
(
  id         BIGINT PRIMARY KEY,
  user_id    BIGINT                   NOT NULL,
  message    TEXT                     NOT NULL,
  cron       VARCHAR(32)              NOT NULL,
  active     BOOLEAN                  NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

ALTER TABLE notification
  DROP COLUMN IF EXISTS notifications_enabled;