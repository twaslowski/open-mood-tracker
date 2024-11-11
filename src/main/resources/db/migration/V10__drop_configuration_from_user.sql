ALTER TABLE "user"
  DROP COLUMN IF EXISTS configuration_id;

ALTER TABLE configuration
  ADD COLUMN IF NOT EXISTS user_id BIGINT;