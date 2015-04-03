-- Table: appointments

-- DROP TABLE appointments;

CREATE TABLE appointments
(
  appointment_id bigserial NOT NULL,
  date date,
  "time" time with time zone,
  location_id bigint NOT NULL,
  report_id bigint NOT NULL,
  purpose text,
  CONSTRAINT appointments_pkey PRIMARY KEY (appointment_id),
  CONSTRAINT appointments_location_fkey FOREIGN KEY (location_id)
      REFERENCES location (location_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT appointments_report_fkey FOREIGN KEY (report_id)
      REFERENCES report (report_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE appointments
  OWNER TO postgres;
