-- Table: item

-- DROP TABLE item;

CREATE TABLE item
(
  item_id bigserial NOT NULL,
  flowchart_id bigint NOT NULL,
  label text,
  pos_top double precision,
  pos_left double precision,
  type character varying,
  CONSTRAINT item_pkey PRIMARY KEY (item_id),
  CONSTRAINT item_flowchart_id_fkey FOREIGN KEY (flowchart_id)
      REFERENCES flowchart (flowchart_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE item
  OWNER TO postgres;
