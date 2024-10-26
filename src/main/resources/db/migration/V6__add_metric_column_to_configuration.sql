ALTER TABLE configuration
  ADD COLUMN metrics VARCHAR[];

ALTER TABLE record
  ADD COLUMN user_id BIGINT;

ALTER TABLE record
  DROP COLUMN IF EXISTS telegram_id;