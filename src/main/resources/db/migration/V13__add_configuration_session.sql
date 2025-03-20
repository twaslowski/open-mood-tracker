CREATE TABLE IF NOT EXISTS configuration_session (
  id VARCHAR(255),
  user_id BIGINT,
  created_at timestamp with time zone,
  updated_at timestamp with time zone
)

