-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- PostgreSQL version: 9.2
-- Project Site: pgmodeler.com.br
-- Model Author: ---

SET check_function_bodies = false;
-- ddl-end --


-- Database creation must be done outside an multicommand file.
-- These commands were put in this file only for convenience.
-- -- object: new_database | type: DATABASE --
-- CREATE DATABASE new_database
-- ;
-- -- ddl-end --
-- 

-- object: public.chats | type: TABLE --
CREATE TABLE public.chats
(
    ID           bigint,
    ID_chat_type smallint,
    title        varchar(200),
    CONSTRAINT chat_pk PRIMARY KEY (ID)

);
-- ddl-end --
-- object: public.chat_types | type: TABLE --
CREATE TABLE public.chat_types
(
    ID   smallint,
    type varchar(20),
    CONSTRAINT chat_type_pk PRIMARY KEY (ID)

);
-- ddl-end --
-- Appended SQL commands --
INSERT
INTO
    public.chat_types
    (ID, type)
VALUES
    (1, 'private');
INSERT
INTO
    public.chat_types
    (ID, type)
VALUES
    (2, 'group');
INSERT
INTO
    public.chat_types
    (ID, type)
VALUES
    (3, 'supergroup');
INSERT
INTO
    public.chat_types
    (ID, type)
VALUES
    (4, 'channel');
-- ddl-end --

-- object: chat_types_fk | type: CONSTRAINT --
ALTER TABLE public.chats
    ADD CONSTRAINT chat_types_fk FOREIGN KEY (ID_chat_type)
        REFERENCES public.chat_types (ID) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;
-- ddl-end --

-- object: public.users | type: TABLE --
CREATE TABLE public.users
(
    ID         bigint,
    user_name  varchar(50),
    first_name varchar(50),
    last_name  varchar(50),
    is_bot     bool,
    birth_date date,
    CONSTRAINT user_id_pk PRIMARY KEY (ID)
);
-- ddl-end --
-- object: public.messages | type: TABLE --
CREATE TABLE public.messages
(
    ID      bigint,
    date    timestamp,
    text    varchar(100),
    ID_user bigint,
    ID_chat bigint,
    CONSTRAINT message_pk PRIMARY KEY (ID, ID_chat)
);
-- ddl-end --
-- object: users_fk | type: CONSTRAINT --
ALTER TABLE public.messages
    ADD CONSTRAINT users_fk FOREIGN KEY (ID_user)
        REFERENCES public.users (ID) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;
-- ddl-end --


-- object: chats_fk | type: CONSTRAINT --
ALTER TABLE public.messages
    ADD CONSTRAINT chats_fk FOREIGN KEY (ID_chat)
        REFERENCES public.chats (ID) MATCH FULL
        ON DELETE SET NULL ON UPDATE CASCADE NOT DEFERRABLE;
-- ddl-end --


-- object: public.user_roles | type: TABLE --
CREATE TABLE public.user_roles
(
    ID   smallint,
    name varchar(20),
    CONSTRAINT role_id_pimary_key PRIMARY KEY (ID)

);
-- ddl-end --
-- Appended SQL commands --
INSERT
INTO
    public.user_roles
    (ID, name)
VALUES
    (0, 'Administrator');
INSERT
INTO
    public.user_roles
    (ID, name)
VALUES
    (1, 'User');
-- ddl-end --

-- object: public.users_user_roles | type: TABLE --
CREATE TABLE public.users_user_roles
(
    ID_user      bigint,
    ID_user_role smallint,
    CONSTRAINT users_user_roles_pk PRIMARY KEY (ID_user, ID_user_role)

);
-- ddl-end --
-- object: users_fk | type: CONSTRAINT --
ALTER TABLE public.users_user_roles
    ADD CONSTRAINT users_fk FOREIGN KEY (ID_user)
        REFERENCES public.users (ID) MATCH FULL
        ON DELETE RESTRICT ON UPDATE CASCADE NOT DEFERRABLE;
-- ddl-end --


-- object: user_roles_fk | type: CONSTRAINT --
ALTER TABLE public.users_user_roles
    ADD CONSTRAINT user_roles_fk FOREIGN KEY (ID_user_role)
        REFERENCES public.user_roles (ID) MATCH FULL
        ON DELETE RESTRICT ON UPDATE CASCADE NOT DEFERRABLE;
-- ddl-end --

-- object: public.ban_list | type: TABLE --
CREATE TABLE public.ban_list
(
    user_id bigint,
    date    timestamp,
    reason  varchar(50)
);
-- ddl-end --
-- object: user_id_fk | type: CONSTRAINT --
ALTER TABLE public.ban_list
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH FULL
        ON DELETE NO ACTION ON UPDATE NO ACTION NOT DEFERRABLE;
-- ddl-end --