--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.10
-- Dumped by pg_dump version 9.5.10

-- Started on 2017-12-21 18:14:43 IST

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
-- TOC entry 182 (class 1259 OID 16701)
-- Name: subpartone; Type: TABLE; Schema: public; Owner: mindreading
--

CREATE TABLE subpartone (
    roll_no bigint NOT NULL,
    tryle text,
    response text
);


ALTER TABLE subpartone OWNER TO mindreading;

--
-- TOC entry 181 (class 1259 OID 16699)
-- Name: subpartone_roll_no_seq; Type: SEQUENCE; Schema: public; Owner: mindreading
--

CREATE SEQUENCE subpartone_roll_no_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE subpartone_roll_no_seq OWNER TO mindreading;

--
-- TOC entry 2145 (class 0 OID 0)
-- Dependencies: 181
-- Name: subpartone_roll_no_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mindreading
--

ALTER SEQUENCE subpartone_roll_no_seq OWNED BY subpartone.roll_no;


--
-- TOC entry 2018 (class 2604 OID 16704)
-- Name: roll_no; Type: DEFAULT; Schema: public; Owner: mindreading
--

ALTER TABLE ONLY subpartone ALTER COLUMN roll_no SET DEFAULT nextval('subpartone_roll_no_seq'::regclass);


--
-- TOC entry 2136 (class 0 OID 16701)
-- Dependencies: 182
-- Data for Name: subpartone; Type: TABLE DATA; Schema: public; Owner: mindreading
--

COPY subpartone (roll_no, tryle, response) FROM stdin;
1	how to do tech?	frist lisen them
\.


--
-- TOC entry 2146 (class 0 OID 0)
-- Dependencies: 181
-- Name: subpartone_roll_no_seq; Type: SEQUENCE SET; Schema: public; Owner: mindreading
--

SELECT pg_catalog.setval('subpartone_roll_no_seq', 1, true);


--
-- TOC entry 2020 (class 2606 OID 16706)
-- Name: subpartone_pkey; Type: CONSTRAINT; Schema: public; Owner: mindreading
--

ALTER TABLE ONLY subpartone
    ADD CONSTRAINT subpartone_pkey PRIMARY KEY (roll_no);


--
-- TOC entry 2143 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2017-12-21 18:14:43 IST

--
-- PostgreSQL database dump complete
--

