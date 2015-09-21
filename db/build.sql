--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

ALTER TABLE ONLY public.links DROP CONSTRAINT links_to_user_fkey;
ALTER TABLE ONLY public.links DROP CONSTRAINT links_from_user_fkey;
ALTER TABLE ONLY public.friends DROP CONSTRAINT friends_b_fkey;
ALTER TABLE ONLY public.friends DROP CONSTRAINT friends_a_fkey;
ALTER TABLE ONLY public.friend_requests DROP CONSTRAINT friend_requests_to_user_fkey;
ALTER TABLE ONLY public.friend_requests DROP CONSTRAINT friend_requests_from_user_fkey;
ALTER TABLE ONLY public.comments DROP CONSTRAINT comments_link_id_fkey;
ALTER TABLE ONLY public.comments DROP CONSTRAINT comments_from_user_fkey;
ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_key;
ALTER TABLE ONLY public.links DROP CONSTRAINT links_pkey;
ALTER TABLE ONLY public.friends DROP CONSTRAINT friends_pkey;
ALTER TABLE ONLY public.friend_requests DROP CONSTRAINT friend_requests_pkey;
ALTER TABLE ONLY public.comments DROP CONSTRAINT comments_pkey;
ALTER TABLE public.users ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.links ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.comments ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE public.users_id_seq;
DROP TABLE public.users;
DROP SEQUENCE public.links_id_seq;
DROP TABLE public.links;
DROP TABLE public.friends;
DROP TABLE public.friend_requests;
DROP SEQUENCE public.comments_id_seq;
DROP TABLE public.comments;
DROP EXTENSION citext;
DROP EXTENSION plpgsql;
DROP SCHEMA public;
--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: citext; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;


--
-- Name: EXTENSION citext; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION citext IS 'data type for case-insensitive character strings';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE comments (
    id bigint NOT NULL,
    link_id integer NOT NULL,
    from_user integer NOT NULL,
    "time" timestamp with time zone NOT NULL,
    data text NOT NULL
);


ALTER TABLE comments OWNER TO postgres;

--
-- Name: comments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE comments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE comments_id_seq OWNER TO postgres;

--
-- Name: comments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE comments_id_seq OWNED BY comments.id;


--
-- Name: friend_requests; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE friend_requests (
    from_user integer NOT NULL,
    to_user integer NOT NULL
);


ALTER TABLE friend_requests OWNER TO postgres;

--
-- Name: friends; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE friends (
    a integer NOT NULL,
    b integer NOT NULL,
    CONSTRAINT friends_check CHECK ((a > b))
);


ALTER TABLE friends OWNER TO postgres;

--
-- Name: links; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE links (
    id integer NOT NULL,
    from_user integer NOT NULL,
    to_user integer NOT NULL,
    "time" timestamp with time zone NOT NULL,
    data jsonb NOT NULL,
    CONSTRAINT links_check CHECK ((from_user <> to_user))
);


ALTER TABLE links OWNER TO postgres;

--
-- Name: links_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE links_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE links_id_seq OWNER TO postgres;

--
-- Name: links_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE links_id_seq OWNED BY links.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE users (
    id integer NOT NULL,
    email citext NOT NULL,
    name text NOT NULL,
    salt text NOT NULL,
    passhash text NOT NULL
);


ALTER TABLE users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comments ALTER COLUMN id SET DEFAULT nextval('comments_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY links ALTER COLUMN id SET DEFAULT nextval('links_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Name: comments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- Name: friend_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY friend_requests
    ADD CONSTRAINT friend_requests_pkey PRIMARY KEY (from_user, to_user);


--
-- Name: friends_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY friends
    ADD CONSTRAINT friends_pkey PRIMARY KEY (a, b);


--
-- Name: links_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY links
    ADD CONSTRAINT links_pkey PRIMARY KEY (id);


--
-- Name: users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: comments_from_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_from_user_fkey FOREIGN KEY (from_user) REFERENCES users(id);


--
-- Name: comments_link_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_link_id_fkey FOREIGN KEY (link_id) REFERENCES links(id);


--
-- Name: friend_requests_from_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY friend_requests
    ADD CONSTRAINT friend_requests_from_user_fkey FOREIGN KEY (from_user) REFERENCES users(id) ON DELETE CASCADE;


--
-- Name: friend_requests_to_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY friend_requests
    ADD CONSTRAINT friend_requests_to_user_fkey FOREIGN KEY (to_user) REFERENCES users(id) ON DELETE CASCADE;


--
-- Name: friends_a_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY friends
    ADD CONSTRAINT friends_a_fkey FOREIGN KEY (a) REFERENCES users(id) ON DELETE CASCADE;


--
-- Name: friends_b_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY friends
    ADD CONSTRAINT friends_b_fkey FOREIGN KEY (b) REFERENCES users(id) ON DELETE CASCADE;


--
-- Name: links_from_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY links
    ADD CONSTRAINT links_from_user_fkey FOREIGN KEY (from_user) REFERENCES users(id);


--
-- Name: links_to_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY links
    ADD CONSTRAINT links_to_user_fkey FOREIGN KEY (to_user) REFERENCES users(id);


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

