-- Table: path

-- DROP TABLE path;

CREATE TABLE path
(
  report_id bigint NOT NULL,
  option_id bigint NOT NULL,
  data text,
  CONSTRAINT path_pkey PRIMARY KEY (report_id, option_id),
  CONSTRAINT path_option_id_fkey FOREIGN KEY (option_id)
      REFERENCES option (option_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT path_report_id_fkey FOREIGN KEY (report_id)
      REFERENCES report (report_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE path
  OWNER TO postgres;
