-- Table: address

-- DROP TABLE address;

CREATE TABLE address
(
  address_id bigserial NOT NULL,
  street character varying,
  city character varying,
  zipcode integer,
  CONSTRAINT address_pkey PRIMARY KEY (address_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE address
  OWNER TO postgres;
