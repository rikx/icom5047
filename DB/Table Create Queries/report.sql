-- Table: report

-- DROP TABLE report;

CREATE TABLE report
(
  report_id bigserial NOT NULL,
  creator_id bigint,
  location_id bigint,
  subject_id bigint,
  flowchart_id bigint,
  note text,
  date_filed date,
  CONSTRAINT report_pkey PRIMARY KEY (report_id),
  CONSTRAINT report_creator_id_fkey FOREIGN KEY (creator_id)
      REFERENCES users (user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT report_flowchart_id_fkey FOREIGN KEY (flowchart_id)
      REFERENCES flowchart (flowchart_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT report_location_id_fkey FOREIGN KEY (location_id)
      REFERENCES location (location_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT report_subject_id_fkey FOREIGN KEY (subject_id)
      REFERENCES person (person_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE report
  OWNER TO postgres;
