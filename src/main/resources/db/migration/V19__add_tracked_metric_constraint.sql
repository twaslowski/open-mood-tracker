ALTER TABLE tracked_metric
  ADD CONSTRAINT uq_user_metric UNIQUE (user_id, metric_id);

ALTER TABLE "metric"
  ADD CONSTRAINT uq_metric_name_user_id UNIQUE (name, owner_id);

ALTER TABLE tracked_metric
  RENAME TO metric_configuration;
ALTER TABLE metric_configuration
  ADD COLUMN tracked BOOLEAN NOT NULL DEFAULT false;