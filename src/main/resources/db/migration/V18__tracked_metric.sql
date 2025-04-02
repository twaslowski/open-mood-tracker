CREATE TABLE tracked_metric
(
  id             VARCHAR PRIMARY KEY,
  user_id        VARCHAR                  NOT NULL,
  metric_id      BIGINT                   NOT NULL,
  created_at     TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at     TIMESTAMP WITH TIME ZONE NOT NULL,
  baseline_value INTEGER                  NOT NULL,

  CONSTRAINT fk_tracked_metric_user FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE,
  CONSTRAINT fk_tracked_metric_metric FOREIGN KEY (metric_id) REFERENCES metric (id) ON DELETE CASCADE,
  CONSTRAINT unique_user_metric UNIQUE (user_id, metric_id)
);

ALTER TABLE "user"
  ADD COLUMN auto_baseline_enabled BOOLEAN NOT NULL DEFAULT FALSE;

DROP TABLE IF EXISTS configuration;