--
-- PostgreSQL database cluster dump
--

\connect postgres

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Roles
--

CREATE ROLE postgres;
ALTER ROLE postgres WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION PASSWORD 'md55385fac320851ce65a40e1e4fd0a1026' VALID UNTIL 'infinity';
CREATE ROLE root;
ALTER ROLE root WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION;






--
-- Database creation
--

CREATE DATABASE "SEA" WITH TEMPLATE = template0 OWNER = postgres;
CREATE DATABASE root WITH TEMPLATE = template0 OWNER = postgres;
REVOKE ALL ON DATABASE template1 FROM PUBLIC;
REVOKE ALL ON DATABASE template1 FROM postgres;
GRANT ALL ON DATABASE template1 TO postgres;
GRANT CONNECT ON DATABASE template1 TO PUBLIC;


\connect "SEA"

SET default_transaction_read_only = off;

--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: address; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE address (
    address_id bigint NOT NULL
);


ALTER TABLE public.address OWNER TO postgres;

--
-- Name: address_address_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE address_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.address_address_id_seq OWNER TO postgres;

--
-- Name: address_address_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE address_address_id_seq OWNED BY address.address_id;


--
-- Name: category; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE category (
    category_id bigint NOT NULL,
    name character varying
);


ALTER TABLE public.category OWNER TO postgres;

--
-- Name: category_category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE category_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.category_category_id_seq OWNER TO postgres;

--
-- Name: category_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE category_category_id_seq OWNED BY category.category_id;


--
-- Name: flowchart; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE flowchart (
    flowchart_id bigint NOT NULL,
    first_id bigint,
    name character varying,
    end_id bigint
);


ALTER TABLE public.flowchart OWNER TO postgres;

--
-- Name: flowchart_flowchart_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE flowchart_flowchart_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.flowchart_flowchart_id_seq OWNER TO postgres;

--
-- Name: flowchart_flowchart_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE flowchart_flowchart_id_seq OWNED BY flowchart.flowchart_id;


--
-- Name: item; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE item (
    item_id bigint NOT NULL,
    flowchart_id bigint NOT NULL,
    label text,
    pos_top double precision,
    pos_left double precision
);


ALTER TABLE public.item OWNER TO postgres;

--
-- Name: item_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE item_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.item_item_id_seq OWNER TO postgres;

--
-- Name: item_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE item_item_id_seq OWNED BY item.item_id;


--
-- Name: location; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE location (
    location_id bigint NOT NULL,
    name character varying,
    address_id bigint,
    owner_id bigint,
    manager_id bigint,
    license character varying,
    agent_id bigint
);


ALTER TABLE public.location OWNER TO postgres;

--
-- Name: location_category; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE location_category (
    location_id bigint NOT NULL,
    category_id bigint NOT NULL
);


ALTER TABLE public.location_category OWNER TO postgres;

--
-- Name: location_location_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE location_location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.location_location_id_seq OWNER TO postgres;

--
-- Name: location_location_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE location_location_id_seq OWNED BY location.location_id;


--
-- Name: option; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE option (
    option_id bigint NOT NULL,
    parent_id bigint,
    next_id bigint,
    label text
);


ALTER TABLE public.option OWNER TO postgres;

--
-- Name: option_option_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE option_option_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.option_option_id_seq OWNER TO postgres;

--
-- Name: option_option_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE option_option_id_seq OWNED BY option.option_id;


--
-- Name: path; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE path (
    report_id bigint NOT NULL,
    option_id bigint NOT NULL
);


ALTER TABLE public.path OWNER TO postgres;

--
-- Name: person; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE person (
    person_id bigint NOT NULL,
    last_name1 character varying NOT NULL,
    first_name character varying NOT NULL,
    email character varying(254) NOT NULL,
    spec_id bigint,
    last_name2 character varying,
    middle_initial character varying(1)
);


ALTER TABLE public.person OWNER TO postgres;

--
-- Name: person_person_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE person_person_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.person_person_id_seq OWNER TO postgres;

--
-- Name: person_person_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE person_person_id_seq OWNED BY person.person_id;


--
-- Name: report; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE report (
    report_id bigint NOT NULL,
    creator_id bigint,
    location_id bigint,
    subject_id bigint,
    flowchart_id bigint
);


ALTER TABLE public.report OWNER TO postgres;

--
-- Name: report_report_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE report_report_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.report_report_id_seq OWNER TO postgres;

--
-- Name: report_report_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE report_report_id_seq OWNED BY report.report_id;


--
-- Name: specialization; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE specialization (
    spec_id bigint NOT NULL,
    name character varying
);


ALTER TABLE public.specialization OWNER TO postgres;

--
-- Name: specialization_spec_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE specialization_spec_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.specialization_spec_id_seq OWNER TO postgres;

--
-- Name: specialization_spec_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE specialization_spec_id_seq OWNED BY specialization.spec_id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE users (
    user_id bigint NOT NULL,
    username character varying(32) NOT NULL,
    passhash text DEFAULT crypt(gen_salt('bf'::text), gen_salt('bf'::text)) NOT NULL,
    person_id bigint,
    salt text DEFAULT gen_salt('bf'::text) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: COLUMN users.passhash; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN users.passhash IS 'crypt(''new password'', gen_salt(''bf''))';


--
-- Name: user_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE user_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_user_id_seq OWNER TO postgres;

--
-- Name: user_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE user_user_id_seq OWNED BY users.user_id;


--
-- Name: view_users; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW view_users AS
    SELECT users.username, person.first_name, person.last_name1, person.last_name2, person.email FROM (users JOIN person ON ((users.person_id = person.person_id)));


ALTER TABLE public.view_users OWNER TO postgres;

--
-- Name: address_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address ALTER COLUMN address_id SET DEFAULT nextval('address_address_id_seq'::regclass);


--
-- Name: category_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY category ALTER COLUMN category_id SET DEFAULT nextval('category_category_id_seq'::regclass);


--
-- Name: flowchart_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY flowchart ALTER COLUMN flowchart_id SET DEFAULT nextval('flowchart_flowchart_id_seq'::regclass);


--
-- Name: item_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item ALTER COLUMN item_id SET DEFAULT nextval('item_item_id_seq'::regclass);


--
-- Name: location_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY location ALTER COLUMN location_id SET DEFAULT nextval('location_location_id_seq'::regclass);


--
-- Name: option_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY option ALTER COLUMN option_id SET DEFAULT nextval('option_option_id_seq'::regclass);


--
-- Name: person_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person ALTER COLUMN person_id SET DEFAULT nextval('person_person_id_seq'::regclass);


--
-- Name: report_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY report ALTER COLUMN report_id SET DEFAULT nextval('report_report_id_seq'::regclass);


--
-- Name: spec_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY specialization ALTER COLUMN spec_id SET DEFAULT nextval('specialization_spec_id_seq'::regclass);


--
-- Name: user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN user_id SET DEFAULT nextval('user_user_id_seq'::regclass);


--
-- Name: address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_pkey PRIMARY KEY (address_id);


--
-- Name: category_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_name_key UNIQUE (name);


--
-- Name: category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_pkey PRIMARY KEY (category_id);


--
-- Name: flowchart_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY flowchart
    ADD CONSTRAINT flowchart_pkey PRIMARY KEY (flowchart_id);


--
-- Name: item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_pkey PRIMARY KEY (item_id);


--
-- Name: location_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY location_category
    ADD CONSTRAINT location_category_pkey PRIMARY KEY (location_id, category_id);


--
-- Name: location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY location
    ADD CONSTRAINT location_pkey PRIMARY KEY (location_id);


--
-- Name: option_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY option
    ADD CONSTRAINT option_pkey PRIMARY KEY (option_id);


--
-- Name: path_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY path
    ADD CONSTRAINT path_pkey PRIMARY KEY (report_id, option_id);


--
-- Name: person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (person_id);


--
-- Name: report_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY report
    ADD CONSTRAINT report_pkey PRIMARY KEY (report_id);


--
-- Name: specialization_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY specialization
    ADD CONSTRAINT specialization_name_key UNIQUE (name);


--
-- Name: specialization_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY specialization
    ADD CONSTRAINT specialization_pkey PRIMARY KEY (spec_id);


--
-- Name: user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);


--
-- Name: user_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT user_username_key UNIQUE (username);


--
-- Name: flowchart_end_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY flowchart
    ADD CONSTRAINT flowchart_end_id_fkey FOREIGN KEY (end_id) REFERENCES item(item_id);


--
-- Name: flowchart_first_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY flowchart
    ADD CONSTRAINT flowchart_first_id_fkey FOREIGN KEY (first_id) REFERENCES item(item_id);


--
-- Name: item_flowchart_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_flowchart_id_fkey FOREIGN KEY (flowchart_id) REFERENCES flowchart(flowchart_id);


--
-- Name: location_address_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY location
    ADD CONSTRAINT location_address_id_fkey FOREIGN KEY (address_id) REFERENCES address(address_id);


--
-- Name: location_agent_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY location
    ADD CONSTRAINT location_agent_id_fkey FOREIGN KEY (agent_id) REFERENCES users(user_id);


--
-- Name: location_category_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY location_category
    ADD CONSTRAINT location_category_category_id_fkey FOREIGN KEY (category_id) REFERENCES category(category_id);


--
-- Name: location_category_location_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY location_category
    ADD CONSTRAINT location_category_location_id_fkey FOREIGN KEY (location_id) REFERENCES location(location_id);


--
-- Name: location_manager_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY location
    ADD CONSTRAINT location_manager_id_fkey FOREIGN KEY (manager_id) REFERENCES person(person_id);


--
-- Name: location_owner_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY location
    ADD CONSTRAINT location_owner_id_fkey FOREIGN KEY (owner_id) REFERENCES person(person_id);


--
-- Name: option_item_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY option
    ADD CONSTRAINT option_item_id_fkey FOREIGN KEY (parent_id) REFERENCES item(item_id);


--
-- Name: option_next_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY option
    ADD CONSTRAINT option_next_id_fkey FOREIGN KEY (next_id) REFERENCES item(item_id);


--
-- Name: path_option_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY path
    ADD CONSTRAINT path_option_id_fkey FOREIGN KEY (option_id) REFERENCES option(option_id);


--
-- Name: path_report_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY path
    ADD CONSTRAINT path_report_id_fkey FOREIGN KEY (report_id) REFERENCES report(report_id);


--
-- Name: person_spec_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_spec_id_fkey FOREIGN KEY (spec_id) REFERENCES specialization(spec_id);


--
-- Name: report_creator_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY report
    ADD CONSTRAINT report_creator_id_fkey FOREIGN KEY (creator_id) REFERENCES users(user_id);


--
-- Name: report_flowchart_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY report
    ADD CONSTRAINT report_flowchart_id_fkey FOREIGN KEY (flowchart_id) REFERENCES flowchart(flowchart_id);


--
-- Name: report_location_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY report
    ADD CONSTRAINT report_location_id_fkey FOREIGN KEY (location_id) REFERENCES location(location_id);


--
-- Name: report_subject_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY report
    ADD CONSTRAINT report_subject_id_fkey FOREIGN KEY (subject_id) REFERENCES person(person_id);


--
-- Name: user_person_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT user_person_fkey FOREIGN KEY (person_id) REFERENCES person(person_id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

\connect postgres

SET default_transaction_read_only = off;

--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: postgres; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE postgres IS 'default administrative connection database';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: userlist; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE userlist (
    user_id bigint NOT NULL,
    username character varying,
    email character varying
);


ALTER TABLE public.userlist OWNER TO postgres;

--
-- Name: TABLE userlist; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON TABLE userlist IS 'test table';


--
-- Name: userlist_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE userlist_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.userlist_user_id_seq OWNER TO postgres;

--
-- Name: userlist_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE userlist_user_id_seq OWNED BY userlist.user_id;


--
-- Name: user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userlist ALTER COLUMN user_id SET DEFAULT nextval('userlist_user_id_seq'::regclass);


--
-- Name: userlist_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY userlist
    ADD CONSTRAINT userlist_pkey PRIMARY KEY (user_id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

\connect root

SET default_transaction_read_only = off;

--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

\connect template1

SET default_transaction_read_only = off;

--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: template1; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE template1 IS 'default template for new databases';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

--
-- PostgreSQL database cluster dump complete
--

