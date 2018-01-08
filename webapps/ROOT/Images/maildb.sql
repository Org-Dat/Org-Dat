--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.10
-- Dumped by pg_dump version 9.5.10

-- Started on 2017-12-21 17:31:31 IST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12393)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2144 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 182 (class 1259 OID 16675)
-- Name: userentry; Type: TABLE; Schema: public; Owner: yahoo
--

CREATE TABLE userentry (
    roll_no bigint NOT NULL,
    name text,
    email text
);


ALTER TABLE userentry OWNER TO yahoo;

--
-- TOC entry 181 (class 1259 OID 16673)
-- Name: userentry_roll_no_seq; Type: SEQUENCE; Schema: public; Owner: yahoo
--

CREATE SEQUENCE userentry_roll_no_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE userentry_roll_no_seq OWNER TO yahoo;

--
-- TOC entry 2145 (class 0 OID 0)
-- Dependencies: 181
-- Name: userentry_roll_no_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: yahoo
--

ALTER SEQUENCE userentry_roll_no_seq OWNED BY userentry.roll_no;


--
-- TOC entry 2018 (class 2604 OID 16678)
-- Name: roll_no; Type: DEFAULT; Schema: public; Owner: yahoo
--

ALTER TABLE ONLY userentry ALTER COLUMN roll_no SET DEFAULT nextval('userentry_roll_no_seq'::regclass);


--
-- TOC entry 2136 (class 0 OID 16675)
-- Dependencies: 182
-- Data for Name: userentry; Type: TABLE DATA; Schema: public; Owner: yahoo
--

COPY userentry (roll_no, name, email) FROM stdin;
1	ponkumar	ponkumar@gmail.com
\.


--
-- TOC entry 2146 (class 0 OID 0)
-- Dependencies: 181
-- Name: userentry_roll_no_seq; Type: SEQUENCE SET; Schema: public; Owner: yahoo
--

SELECT pg_catalog.setval('userentry_roll_no_seq', 1, true);


--
-- TOC entry 2020 (class 2606 OID 16680)
-- Name: userentry_pkey; Type: CONSTRAINT; Schema: public; Owner: yahoo
--

ALTER TABLE ONLY userentry
    ADD CONSTRAINT userentry_pkey PRIMARY KEY (roll_no);


--
-- TOC entry 2143 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2017-12-21 17:31:31 IST

--
-- PostgreSQL database dump complete
--

