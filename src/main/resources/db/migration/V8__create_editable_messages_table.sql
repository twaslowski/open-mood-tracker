CREATE TABLE IF NOT EXISTS editable_message
(
  chat_id    BIGINT PRIMARY KEY NOT NULL,
  message_id BIGINT             NOT NULL
);