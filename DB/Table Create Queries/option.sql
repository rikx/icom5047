-- Table: option

-- DROP TABLE option;

CREATE TABLE option
(
  option_id bigserial NOT NULL,
  parent_id bigint,
  next_id bigint,
  label text,
  CONSTRAINT option_pkey PRIMARY KEY (option_id),
  CONSTRAINT option_item_id_fkey FOREIGN KEY (parent_id)
      REFERENCES item (item_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT option_next_id_fkey FOREIGN KEY (next_id)
      REFERENCES item (item_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE option
  OWNER TO postgres;
