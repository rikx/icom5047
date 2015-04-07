-- Table: address

-- DROP TABLE address;

CREATE TABLE address
(
  address_id bigserial NOT NULL,
  address_line1 character varying,
  city character varying,
  zipcode integer,
  address_line2 character varying,
  CONSTRAINT address_pkey PRIMARY KEY (address_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE address
  OWNER TO postgres;
