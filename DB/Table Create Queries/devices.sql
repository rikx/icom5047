-- Table: devices

-- DROP TABLE devices;

CREATE TABLE devices
(
  device_id bigserial NOT NULL,
  name character varying,
  id_number character varying NOT NULL,
  user_id bigint,
  latest_sync timestamp with time zone,
  CONSTRAINT devices_pkey PRIMARY KEY (device_id),
  CONSTRAINT devices_users_fkey FOREIGN KEY (user_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE devices
  OWNER TO postgres;
