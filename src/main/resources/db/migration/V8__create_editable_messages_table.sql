CREATE TABLE IF NOT EXISTS editable_message
(
  chat_id            BIGINT PRIMARY KEY       NOT NULL,
  message_id         INTEGER                  NOT NULL,
  creation_timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);