ALTER TABLE "user" ALTER COLUMN id TYPE VARCHAR(255);
ALTER TABLE "record" ALTER COLUMN user_id TYPE VARCHAR(255);
ALTER TABLE "configuration" ALTER COLUMN user_id TYPE VARCHAR(255);
ALTER TABLE "notification" ALTER COLUMN user_id TYPE VARCHAR(255);

DROP SEQUENCE IF EXISTS user_id_seq;

DROP TABLE IF EXISTS configuration_session;