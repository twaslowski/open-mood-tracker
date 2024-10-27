ALTER TABLE configuration
  ADD COLUMN tracked_metric_ids VARCHAR[];

ALTER TABLE record
  ADD COLUMN user_id BIGINT;

ALTER TABLE record
  DROP COLUMN IF EXISTS telegram_id;