-- Table: flowchart

-- DROP TABLE flowchart;

CREATE TABLE flowchart
(
  flowchart_id bigserial NOT NULL,
  first_id bigint,
  name character varying,
  end_id bigint,
  creator_id bigint,
  version character varying,
  CONSTRAINT flowchart_pkey PRIMARY KEY (flowchart_id),
  CONSTRAINT flowchart_creator_id_fkey FOREIGN KEY (creator_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT flowchart_end_id_fkey FOREIGN KEY (end_id)
      REFERENCES item (item_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT flowchart_first_id_fkey FOREIGN KEY (first_id)
      REFERENCES item (item_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE flowchart
  OWNER TO postgres;
