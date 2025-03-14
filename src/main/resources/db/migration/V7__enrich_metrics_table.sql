ALTER TABLE metric
  ADD COLUMN owner_id BIGINT NOT NULL DEFAULT 1;

ALTER TABLE metric
  ADD COLUMN creation_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now();

ALTER TABLE metric
  ADD COLUMN update_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now();

ALTER TABLE metric
  ADD COLUMN sort_order VARCHAR NOT NULL DEFAULT 'ASC';